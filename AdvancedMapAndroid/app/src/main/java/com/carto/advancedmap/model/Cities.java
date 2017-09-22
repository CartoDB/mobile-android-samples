package com.carto.advancedmap.model;

import com.carto.advancedmap.utils.BoundingBox;

/**
 * Created by aareundo on 22/09/2017.
 */

public class Cities {

    public static City[] LIST = {
            new City("Berlin", new BoundingBox(13.2285, 13.5046, 52.4698, 52.57477)),

            new City("New York", new BoundingBox(-74.1205, -73.4768, 40.4621, 41.0043)),

            new City("Madrid", new BoundingBox(-3.7427, -3.6432, 40.3825, 40.4904)),

            new City("Paris", new BoundingBox(2.1814, 2.4356, 48.8089, 48.9176)),

            new City("San Francisco", new BoundingBox(-122.5483, -122.3382, 37.6642, 37.8173)),

            new City("London", new BoundingBox(-0.5036, 0.3276, 51.2871, 51.6939)),

            new City("Mexico City", new BoundingBox(-99.329453, -98.937378, 19.251515, 19.608956)),

            new City("Barcelona", new BoundingBox(2.098732, 2.249451, 41.345629, 41.454049)),

            new City("Tartu", new BoundingBox(26.6548, 26.7901, 58.3404, 58.3964)),

            new City("New Delhi", new BoundingBox(77.1477, 77.2757, 28.5361, 28.6368))

    };
}
