package com.foraconnect.fora;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    private ListView listView;

    ArrayList<LiveEventItem> liveEvents;
    ArrayList<FutureEventItem> futureEvents;

    String loc;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            openMap();
        }
        else if (id == R.id.nav_live) {
            openLive();

        }
        else if (id == R.id.nav_future) {
            openFuture();
        }
        else if (id == R.id.nav_history) {

        }
        else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PlaceMarker pm = new PlaceMarker(this);
        mMap.setInfoWindowAdapter(pm);
        Log.i("AHHH", "onMapReady");
        try {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    setMap();
                    return false;
                }
            });
            Log.i("AHHH", "Set on Location Listener");
            userLocationFAB();
            userUberFAB();
            setMap();
        } catch (SecurityException e) {
            Toast.makeText(MainActivity.this,
                    "Please enable location services", Toast.LENGTH_LONG).show();
            Log.i("AHHH", "Security Exception");
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        mMap.setMyLocationEnabled(true);
                        setMap();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Location services not enabled", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setMap() {
        mMap.clear();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        loc = "Stamford";
        Log.i("AHHH", "Setting Map");
        try {
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                LatLng stm = new LatLng(41.0534, -73.5387);
                LatLng nwh = new LatLng(41.3083, -72.9279);
                LatLng nor = new LatLng(41.1177, -73.4082);

                double sDistance = CalculationByDistance(latLng, stm);
                double hDistance = CalculationByDistance(latLng, nwh);
                double nDistance = CalculationByDistance(latLng, nor);

                if(sDistance >= hDistance && nDistance >= hDistance)
                    loc = "New Haven";
                else if(sDistance >= nDistance && hDistance >= nDistance)
                    loc = "Norwalk";
                else
                    loc = "Stamford";
            }
        } catch (SecurityException e) {
            loc = "Stamford";
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            loc = "Stamford";
        } catch (Exception e) {
            e.printStackTrace();
            loc = "Stamford";
        }
        Log.i("AHHH", "adding places...");
        if(loc.equals("New Haven")) {
            addPlace("Geronimo's", 382, 62, 41.306643, -72.930729, true);
            addPlace("Brother Jimmy's", 354, 42, 41.305250, -72.928478, true);
            addPlace("BAR", 198, 45, 41.306096, -72.930278, true);
            addPlace("Olives and Oils", 173, 53, 41.305559, -72.928043, true);
            addPlace("Barcelona", 149, 51, 41.305419, -72.927547, true);

            addPlace("Yale Bulldogs Hockey", 186, 50, 41.316829, -72.925026, false);
            addPlace("Jazz Thursday", 112, 50, 41.308188, -72.931204, false);
            addPlace("Badfish", 79, 50, 41.311528, -72.929635, false);
        } else if(loc.equals("Norwalk")) {
            addPlace("Salt Water",209, 61, 41.098709, -73.416832, true);
            addPlace("Soho", 187, 42, 41.099237, -73.417768, true);
            addPlace("Our House",176, 48, 41.098763, -73.417454, true);
        } else {
            addPlace("Brother Jimmy's",382, 62,41.056207, -73.538967, true);
            addPlace("Hudson Social", 354, 42, 41.056316, -73.538651, true);
            addPlace("Rivera Maya", 198, 45, 41.053396, -73.540980, true);
            addPlace("Palms", 173, 53, 41.053553, -73.539733, true);

            addPlace("Brick House", 186, 50, 41.057049, -73.538305, false);
            addPlace("Seaside Tavern", 112, 50, 41.048768, -73.509174, false);
            addPlace("Sign of Whale", 79, 50, 41.039860, -73.542507, false);
        }
        Log.i("AHHH", "Added Places");
        loadEvents();
    }

    private void addPlace(String t, int p, int b, double v, double v1, boolean l) {
        String s = p+"~"+b;
        LatLng latLng = new LatLng(v, v1);

        if(l) {
            mMap.addMarker(new MarkerOptions().position(latLng).title(t).snippet(s).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        else
            mMap.addMarker(new MarkerOptions().position(latLng).title(t).snippet(s));
    }

    private void loadEvents() {
        listView = findViewById(R.id.eventsList);
        liveEvents = new ArrayList<>();
        futureEvents = new ArrayList<>();
        Log.i("AHHH", "Loading Events");
        if(loc.equals("New Haven")) {
            liveEvents.add(new LiveEventItem("Geronimo's", 382, 51, null));
            liveEvents.add(new LiveEventItem("Brother Jimmy's", 354, 54,
                    getResources().getDrawable(R.drawable.clubpic1)));
            liveEvents.add(new LiveEventItem("BAR", 198, 43,
                    getResources().getDrawable(R.drawable.clubpic2)));
            liveEvents.add(new LiveEventItem("Olives and Oils", 173, 48,
                    getResources().getDrawable(R.drawable.clubpic3)));
            liveEvents.add(new LiveEventItem("Barcelona", 149, 48,
                    getResources().getDrawable(R.drawable.clubpic3)));

            futureEvents.add(new FutureEventItem("Yale Bulldogs Hockey", 186, true,
                    getResources().getDrawable(R.drawable.clublogo1)));
            futureEvents.add(new FutureEventItem("Jazz Thursday", 112, false,
                    getResources().getDrawable(R.drawable.clublogo2)));
            futureEvents.add(new FutureEventItem("Badfish", 79, false,
                    getResources().getDrawable(R.drawable.clublogo3)));
        } else if(loc.equals("Norwalk")) {
            liveEvents.add(new LiveEventItem("Salt Water", 209, 51, null));
            liveEvents.add(new LiveEventItem("Soho", 187, 48, null));
            liveEvents.add(new LiveEventItem("Our House", 176, 52, null));

            futureEvents.add(new FutureEventItem("Salt Water", 165, true, null));
            futureEvents.add(new FutureEventItem("Soho", 154, false, null));
            futureEvents.add(new FutureEventItem("Our House", 149, false, null));
        } else {
            liveEvents.add(new LiveEventItem("Brother Jimmy's", 382, 54,
                    getResources().getDrawable(R.drawable.clubpic1)));
            liveEvents.add(new LiveEventItem("Hudson Social", 354, 43,
                    getResources().getDrawable(R.drawable.clubpic2)));
            liveEvents.add(new LiveEventItem("Rivera Maya", 198, 48,
                    getResources().getDrawable(R.drawable.clubpic3)));
            liveEvents.add(new LiveEventItem("Palms", 173, 48,
                    getResources().getDrawable(R.drawable.clubpic3)));

            futureEvents.add(new FutureEventItem("Brick House", 186, true,
                    getResources().getDrawable(R.drawable.clublogo1)));
            futureEvents.add(new FutureEventItem("Seaside Tavern", 112, false,
                    getResources().getDrawable(R.drawable.clublogo2)));
            futureEvents.add(new FutureEventItem("Sign of Whale", 79, false,
                    getResources().getDrawable(R.drawable.clublogo3)));
        }
        Log.i("AHHH", "Loaded Events");
        openMap();
    }

    private void openMap() {
        listView.setVisibility(View.GONE);
        supportMapFragment.getView().setVisibility(View.VISIBLE);
        Log.i("AHHH", "Map Visible");
    }

    private void openLive() {
        listView.setVisibility(View.VISIBLE);
        supportMapFragment.getView().setVisibility(View.GONE);
        listView.setAdapter(new LiveAdapter(MainActivity.this, liveEvents));
        Log.i("AHHH", "Open Live");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                navigationView.getMenu().getItem(0).setChecked(true);
                openMap();

                if(loc.equals("New Haven")) {
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.306643, -72.930729), 17));
                    else if (i == 1)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.305250, -72.928478), 17));
                    else if (i == 2)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.306096, -72.930278), 17));
                    else if (i == 3)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.305559, -72.928043), 17));
                    else if (i == 4)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.305419, -72.927547), 17));
                } else if(loc.equals("Norwalk")) {
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.098709, -73.416832), 17));
                    else if (i == 1)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.099237, -73.417768), 17));
                    else if (i == 2)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.098763, -73.417454), 17));
                } else {
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.056207, -73.538967), 17));
                    else if (i == 1)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.056316, -73.538651), 17));
                    else if (i == 2)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.053396, -73.540980), 17));
                    else if (i == 3)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.053553, -73.539733), 17));
                }
            }
        });
    }

    private void openFuture() {
        listView.setVisibility(View.VISIBLE);
        supportMapFragment.getView().setVisibility(View.GONE);
        listView.setAdapter(new FutureAdapter(MainActivity.this, futureEvents));
        Log.i("AHHH", "Open Future");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                navigationView.getMenu().getItem(0).setChecked(true);
                openMap();

                if(loc.equals("New Haven")) {
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.316829, -72.925026), 17));
                    else if (i == 1)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.308188, -72.931204), 17));
                    else if (i == 2)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.311528, -72.929635), 17));
                } else if(loc.equals("Norwalk")) {
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.098709, -73.416832), 17));
                    else if (i == 1)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.099237, -73.417768), 17));
                    else if (i == 2)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.098763, -73.417454), 17));
                } else {
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.057049, -73.538305), 17));
                    else if (i == 1)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.048768, -73.509174), 17));
                    else if (i == 2)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.039860, -73.542507), 17));
                }
            }
        });
    }

    private void userLocationFAB(){
        Log.i("AHHH", "Location Fab starting");
        FloatingActionButton FAB = findViewById(R.id.myLocationButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(0).setChecked(true);
                setMap();
            }
        });
        Log.i("AHHH", "Location Fab Done");
    }

    private void userUberFAB() {
        Log.i("AHHH", "Uber App Starting");
        FloatingActionButton FAB = findViewById(R.id.uberButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getPackageManager();
                try {
                    pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                    String uri = "uber://?action=setPickup&pickup=my_location";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
                    }
                }
            }
        });
        Log.i("AHHH", "Uber app done");
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
