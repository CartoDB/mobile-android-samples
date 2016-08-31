package com.carto.advancedmap.map;

import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.mapbase.MapSampleBaseActivity;
import com.carto.advancedmap.datasource.MyMergedRasterTileDataSource;
import com.carto.advancedmap.util.Const;
import com.carto.core.MapPos;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.RasterTileLayer;

/**
 * A sample demonstrating how to create and use custom raster tile data source.
 * MyMergedRasterTileDataSource uses two input tile data sources to
 * create blended tile bitmaps. This can be faster than using two separate raster layers
 * and takes less memory.
 * 
 * Compare with RasterOverlayActivity which shows same rasters as separate layers
 * 
 */
@Description(value = "Customized raster tile data source")
public class CustomRasterDataSourceActivity extends MapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize base and hillshade data sources
        TileDataSource baseTileDataSource = new HTTPTileDataSource(0, 24, Const.MAPBOX_RASTER_URL);
        TileDataSource hillshadeTileDataSource = new HTTPTileDataSource(0, 24, Const.HILLSHADE_RASTER_URL);
        
        // Create merged raster data source
        TileDataSource mergedTileDataSource = new MyMergedRasterTileDataSource(baseTileDataSource, hillshadeTileDataSource);

        // Create raster layer
        baseLayer = new RasterTileLayer(mergedTileDataSource);
        mapView.getLayers().add(baseLayer);
        
        // finally animate map to a nice place
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(-122.4323, 37.7582)), 1);
        mapView.setZoom(13, 1);
    }
}
