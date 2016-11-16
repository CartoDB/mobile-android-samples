package com.carto.advancedmap.sections.basemap.model;

import java.util.ArrayList;

/**
 * Created by aareundo on 08/11/16.
 */

public class Section
{
    private NameValuePair OSM;

    private SectionType type;

    private ArrayList<NameValuePair> styles;

    public Section(NameValuePair osm, SectionType type, ArrayList<NameValuePair> styles)
    {
        this.OSM = osm;
        this.type = type;
        this.styles = styles;
    }

    public SectionType getType()
    {
        return type;
    }

    public NameValuePair getOSM()
    {
        return OSM;
    }

    public ArrayList<NameValuePair> getStyles()
    {
        return styles;
    }

    public boolean isEqual(Section section)
    {
        return this.OSM.getValue() == section.OSM.getValue();
    }
}
