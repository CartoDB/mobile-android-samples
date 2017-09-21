package com.carto.advancedmap.model;

/**
 * Created by aareundo on 21/09/2017.
 */

public class Sample {

    public int imageResource;

    public String title, description;

    public Class<?> activity;

    public Sample(int resource, String title, String description, Class activity) {
        this.imageResource = resource;
        this.title = title;
        this.description = description;
        this.activity = activity;
    }
}
