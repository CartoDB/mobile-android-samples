package com.carto.cartomap.main;

import com.carto.cartomap.R;
import com.carto.cartomap.sections.mapsapi.AnonymousRasterTableActivity;
import com.carto.cartomap.sections.mapsapi.AnonymousVectorTableActivity;
import com.carto.cartomap.sections.mapsapi.NamedMapActivity;
import com.carto.cartomap.sections.sqlapi.SQLServiceActivity;
import com.carto.cartomap.sections.torqueapi.TorqueShipActivity;

/**
 * Created by aareundo on 28/09/2017.
 */

public class Samples {

    public static Sample[] LIST = {
            new Sample(
                    R.drawable.icon_sample_raster_tile,
                    "Raster Tile",
                    "Anonymous raster tiles via CARTO maps service",
                    AnonymousRasterTableActivity.class),
            new Sample(
                    R.drawable.icon_sample_vector_tile,
                    "Vector Tile",
                    "Anonymous vector tiles via CARTO maps service",
                    AnonymousVectorTableActivity.class),
            new Sample(
                    R.drawable.icon_sample_named_map,
                    "Indoor Map",
                    "Names map via CARTO maps service",
                    NamedMapActivity.class),
            new Sample(
                    R.drawable.icon_sample_sql_service,
                    "Large Cities",
                    "Largest cities via CARTO SQL Service",
                    SQLServiceActivity.class),
            new Sample(
                    R.drawable.icon_sample_torque,
                    "Indoor Torque",
                    "Torque map of movement in a shopping mal",
                    TorqueShipActivity.class),
    };
}
