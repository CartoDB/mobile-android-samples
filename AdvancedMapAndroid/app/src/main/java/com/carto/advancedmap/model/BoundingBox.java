package com.carto.advancedmap.model;

import com.carto.core.MapPos;

import java.util.Locale;

/**
 * Created by aareundo on 14/03/17.
 */


public class BoundingBox
{
    public double minLat;

    public double minLon;

    public double maxLat;

    public double maxLon;

    public BoundingBox(double minLon, double maxLon, double minLat, double maxLat) {
        this.minLat = minLat;
        this.minLon = minLon;
        this.maxLat = maxLat;
        this.maxLon = maxLon;
    }

    public MapPos getCenter() {
        return new MapPos((maxLon + minLon) / 2, (maxLat + minLat) / 2);
    }

    public String toString()
    {
        return String.format(Locale.US, "bbox(" + minLon + "," + minLat + "," + maxLon + "," + maxLat + ")", null);
    }
}
