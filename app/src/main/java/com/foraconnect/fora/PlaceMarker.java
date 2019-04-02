package com.foraconnect.fora;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class PlaceMarker implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public PlaceMarker(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.place_marker, null);

        String[] data = marker.getSnippet().split("~");
        Log.i("AHHH", data[0]+" "+data[1]);
        int people = Integer.parseInt(data[0]);
        int pBoys = Integer.parseInt(data[1]);

        TextView percentView = view.findViewById(R.id.markerPercent);
        TextView peopleView = view.findViewById(R.id.markerPeople);
        TextView titleView = view.findViewById(R.id.markerTitle);

        String percentString = pBoys+"%";
        String peopleString = people+" People";
        String titleString = marker.getTitle();

        percentView.setText(percentString);
        peopleView.setText(peopleString);
        titleView.setText(titleString);

        if(pBoys > 50) {
            percentView.setTextColor(Color.parseColor("#1E90FF"));
            peopleView.setTextColor(Color.parseColor("#1E90FF"));
            titleView.setTextColor(Color.parseColor("#1E90FF"));
        }
        if(pBoys < 50) {
            percentView.setTextColor(Color.parseColor("#FF69B4"));
            peopleView.setTextColor(Color.parseColor("#FF69B4"));
            titleView.setTextColor(Color.parseColor("#FF69B4"));
        }

        RelativeLayout canvas = (RelativeLayout) view.findViewById(R.id.markerCanvasLayout);
        canvas.addView(new ScaleView(context, people, pBoys));
        return view;
    }
}
