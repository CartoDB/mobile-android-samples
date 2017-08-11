package com.carto.advanced.kotlin.model

import com.carto.advanced.kotlin.utils.BoundingBox

/**
 * Created by aareundo on 13/07/2017.
 */
class Cities {

    companion object {
        val list: MutableList<City> = mutableListOf(

                City("Berlin", BoundingBox(13.2285, 13.5046, 52.4698, 52.57477)),

                City("New York", BoundingBox(-74.1205, -73.4768, 40.4621, 41.0043)),

                City("Madrid", BoundingBox(-3.7427, -3.6432, 40.3825, 40.4904)),

                City("Paris", BoundingBox(2.1814, 2.4356, 48.8089, 48.9176)),

                City("San Francisco", BoundingBox(-122.5483, -122.3382, 37.6642, 37.8173)),

                City("London", BoundingBox(-0.5036, 0.3276, 51.2871, 51.6939)),

                City("Mexico City", BoundingBox(-99.329453, -98.937378, 19.251515, 19.608956)),

                City("Barcelona", BoundingBox(2.098732, 2.249451, 41.345629, 41.454049)),

                City("Tartu", BoundingBox(26.6548, 26.7901, 58.3404, 58.3964)),

                City("New Delhi", BoundingBox(77.1477, 77.2757, 28.5361, 28.6368))
        )
    }
}