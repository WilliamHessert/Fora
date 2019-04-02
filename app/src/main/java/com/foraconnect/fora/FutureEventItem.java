package com.foraconnect.fora;

import android.graphics.drawable.Drawable;

/**
 * Created by williamhessert on 7/15/18.
 */

public class FutureEventItem {

    private String title;
    private int people;
    private boolean selected;
    private Drawable image;

    public FutureEventItem(String title, int people, boolean selected, Drawable image) {
        this.title = title;
        this.people = people;
        this.selected = selected;
        this.image = image;
    }

    public String getTitle() { return title; }

    public int getPeople() { return  people; }

    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean getSelected() { return selected; }

    public Drawable getImage() { return image; }
}
