package com.carto.advancedmap.sections.basemap.model;

/**
 * Created by aareundo on 08/11/16.
 */

public class NameValuePair
{
    private String name;

    private String value;

    public NameValuePair(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }
}
