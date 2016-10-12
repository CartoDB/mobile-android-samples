package com.carto.advancedmap.mapsamples;

import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.advancedmap.util.Const;
import com.carto.core.MapPos;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.PersistentCacheTileDataSource;
import com.carto.layers.RasterTileLayer;

/**
 * A sample demonstrating how to use raster layer on top of the vector base map to provide height information.
 */
@Description(value = "Exteral raster layer on top of the vector base map")
public class RasterOverlayActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);
        
        // Initialize hillshading raster data source, better visible in mountain ranges
        HTTPTileDataSource hillsRasterTileDataSource = new HTTPTileDataSource(0, 24, Const.HILLSHADE_RASTER_URL);

        // Add persistent caching datasource, tiles will be stored locally on persistent storage
        PersistentCacheTileDataSource cachedDataSource = 
                new PersistentCacheTileDataSource(hillsRasterTileDataSource, getExternalFilesDir(null)+"/mapcache_hills.db");
        
        // Initialize a raster layer with the previous data source
        RasterTileLayer hillshadeLayer = new RasterTileLayer(cachedDataSource);
        // Add the previous raster layer to the map
        mapView.getLayers().add(hillshadeLayer);

        // finally animate map to a nice place
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(-122.4323, 37.7582)), 1);
        mapView.setZoom(13, 1);
    }
}
