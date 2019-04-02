package com.foraconnect.fora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by williamhessert on 7/15/18.
 */

public class LiveAdapter extends BaseAdapter {

    Context context;
    ArrayList<LiveEventItem> items;
    private static LayoutInflater inflater = null;

    public LiveAdapter(Context context, ArrayList<LiveEventItem> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.live_event_item, null);

        LiveEventItem item = items.get(position);

        TextView title = (TextView) vi.findViewById(R.id.eventTitle);
        title.setText(item.getTitle());

        TextView peopleText = (TextView) vi.findViewById(R.id.eventPeople);
        String people = item.getPeople()+"+ People";
        peopleText.setText(people);

//        TextView girlsText = (TextView) vi.findViewById(R.id.girlText);
//        int girls = 100 - item.getPercentGirls();
//        String girlString = girls+"%";
//        girlsText.setText(girlString);
//
//        TextView boysText = (TextView) vi.findViewById(R.id.boyText);
//        int boys = 100 - girls;
//        String boyString = boys+"%";
//        boysText.setText(boyString);

//        ImageView image = (ImageView) vi.findViewById(R.id.eventImage);
//        image.setImageDrawable(item.getImage());

        return vi;
    }
}
