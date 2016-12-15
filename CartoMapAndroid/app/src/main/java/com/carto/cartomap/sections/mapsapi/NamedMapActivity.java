package com.carto.cartomap.sections.mapsapi;

import android.os.Bundle;

import com.carto.cartomap.sections.BaseMapActivity;
import com.carto.cartomap.util.ActivityData;
import com.carto.core.MapPos;
import com.carto.core.StringVariantMap;
import com.carto.layers.Layer;
import com.carto.layers.LayerVector;
import com.carto.services.CartoMapsService;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

@ActivityData(name = "Named Map", description = "CARTO data as Vector Tiles from a named map")
public class NamedMapActivity extends BaseMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // BaseMapActivity creates and sets mapView
        super.onCreate(savedInstanceState);

        final CartoMapsService service = new CartoMapsService();

        // Use raster layers, not vector
        service.setDefaultVectorLayerMode(true);

        service.setUsername("nutiteq");

        final String name = "tpl_69f3eebe_33b6_11e6_8634_0e5db1731f59";

        // Be sure to make network queries on another thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    LayerVector layers = service.buildNamedMap(name, new StringVariantMap());

                    for (int i = 0; i < layers.size(); i++) {
                        Layer layer = layers.get(i);
                        mapView.getLayers().add(layer);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        MapPos position = baseProjection.fromLatLong(37.32549682016584, -121.94595158100128);
        mapView.setFocusPos(position, 0);
        mapView.setZoom(19, 1);
    }
}
