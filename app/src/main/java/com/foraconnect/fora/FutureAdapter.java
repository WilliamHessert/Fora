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

public class FutureAdapter extends BaseAdapter {

    Context context;
    ArrayList<FutureEventItem> items;
    private static LayoutInflater inflater = null;

    public FutureAdapter(Context context, ArrayList<FutureEventItem> items) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.future_event_item, null);

        final View view = vi;

        final FutureEventItem item = items.get(position);

        TextView title = (TextView) vi.findViewById(R.id.fTitle);
        title.setText(item.getTitle());

        TextView people = (TextView) vi.findViewById(R.id.fPeople);
        int peopleGoing = item.getPeople();
        String peopleString = peopleGoing+" Going";
        people.setText(peopleString);

//        ImageView image = (ImageView) vi.findViewById(R.id.fImage);
//        image.setImageDrawable(item.getImage());
//
//        final ImageView star = (ImageView) vi.findViewById(R.id.selectEvent);
//        if(item.getSelected())
//            star.setImageDrawable(
//                    context.getResources().getDrawable(android.R.drawable.btn_star_big_on));
//        star.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!item.getSelected()) {
//                    items.get(position).setSelected(true);
//                    star.setImageDrawable(context
//                            .getResources().getDrawable(android.R.drawable.btn_star_big_on));
//
//                    changePeopleText(view, true);
//                }
//                else {
//                    items.get(position).setSelected(false);
//                    star.setImageDrawable(context
//                            .getResources().getDrawable(android.R.drawable.btn_star_big_off));
//
//                    changePeopleText(view, false);
//                }
//            }
//        });

        return vi;
    }

    private void changePeopleText(View v, boolean add) {
        TextView people = (TextView) v.findViewById(R.id.fPeople);
        String peopleString = people.getText().toString();
        String peopleNumString = peopleString.substring(0, peopleString.indexOf(" "));

        int peopleNum = Integer.parseInt(peopleNumString);

        if(add)
            peopleNum++;
        else
            peopleNum--;

        peopleString = peopleNum+" People Going";
        people.setText(peopleString);
    }
}
