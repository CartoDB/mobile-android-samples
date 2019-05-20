package com.carto.advancedmap.model;

public class MapOption {

    public String name, tag;
    public boolean value;

    public MapOption(String name, String tag, boolean value) {
        this.name = name;
        this.tag = tag;
        this.value = value;
    }
}
