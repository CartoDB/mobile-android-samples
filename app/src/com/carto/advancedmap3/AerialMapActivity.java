package com.carto.advancedmap3;

import android.os.Bundle;

import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.RasterTileLayer;

/**
 * A sample demonstrating how to use raster layers with external
 * tile data sources.
 */
public class AerialMapActivity extends MapSampleBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize a Bing raster data source. Note: tiles start from level 1, there is no single root tile!
		TileDataSource baseRasterTileDataSource = new HTTPTileDataSource(1, 19, "https://ecn.t3.tiles.virtualearth.net/tiles/a{quadkey}.jpeg?g=471&mkt=en-US");
        
		// Create raster layer
		baseLayer = new RasterTileLayer(baseRasterTileDataSource);
		mapView.getLayers().add(baseLayer);
	}
}
