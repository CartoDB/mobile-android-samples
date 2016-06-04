package com.carto.advancedmap3;

import android.os.Bundle;

import com.carto.core.MapPos;
import com.carto.core.Variant;
import com.carto.services.CartoMapsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A sample demonstrating how to use CartoDB PostGIS Raster data, as tiled raster layer
 * Inspired by web sample http://bl.ocks.org/jorgeas80/4c7169c9b6356858f3cc
 */
public class CartoDBRasterTileActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);

        // define server config
        JSONObject configJson = new JSONObject();
        try {

            // you need to change these according to your DB
            String sql = "select * from table_46g";
            String cartoCss =
                    "#table_46g {raster-opacity: 0.5;}";

            // you probably do not need to change much of below
            configJson.put("version", "1.2.0");

            JSONArray layersArrayJson = new JSONArray();
            JSONObject layersJson = new JSONObject();
            layersJson.put("type", "cartodb");

            JSONObject optionsJson = new JSONObject();
            optionsJson.put("sql", sql);
            optionsJson.put("cartocss", cartoCss);
            optionsJson.put("cartocss_version", "2.3.0");
            optionsJson.put("geom_column", "the_raster_webmercator");
            optionsJson.put("geom_type", "raster");
            layersJson.put("options", optionsJson);
            layersArrayJson.put(layersJson);
            configJson.put("layers", layersArrayJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        final String config = configJson.toString();

        // Use the Maps service to configure layers. Note that this must be done
        // in a separate thread on Android, as Maps API requires connecting to server
        // which is not allowed in main thread.
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				CartoMapsService mapsService = new CartoMapsService();
				mapsService.setUsername("nutiteq");
				mapsService.setDefaultVectorLayerMode(false); // use raster layers, not vector layers
				mapsService.buildMap(mapView.getLayers(), Variant.fromString(config));
			}
		});
		serviceThread.start();

        // finally go map to the content area
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(22.7478235498916, 58.8330577553785)), 0);
        mapView.setZoom(11, 0);
    }

}
