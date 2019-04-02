package com.foraconnect.fora;

import android.graphics.drawable.Drawable;

/**
 * Created by williamhessert on 7/15/18.
 */

public class LiveEventItem {

    private String title;
    private int people;
    private int percentGirls;
    private Drawable image;

    public LiveEventItem(String title, int people, int percentGirls, Drawable image) {
        this.title = title;
        this.people = people;
        this.percentGirls = percentGirls;
        this.image = image;
    }

    public String getTitle() { return title; }

    public int getPeople() { return  people; }

    public int getPercentGirls() { return percentGirls; }

    public Drawable getImage() { return image; }
}
