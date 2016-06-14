package com.carto.advancedmap3.datasource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.carto.core.MapTile;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.components.TileData;
import com.carto.utils.BitmapUtils;
import com.carto.utils.Log;

import java.io.IOException;
import java.net.URL;

/**
 * A minimal custom raster tile data source which uses app-level HTTP requests
 * It supports e.g. HTTPS
 */
public class MyHttpTileDataSource extends HTTPTileDataSource {

	public MyHttpTileDataSource(int minZoom, int maxZoom, String baseURL) {
		super(minZoom, maxZoom, baseURL);
	}

	public TileData loadTile(MapTile tile) {

		String urlString = super.buildTileURL(getBaseURL(), tile);

        Log.debug("requesting tile: "+urlString);

        Bitmap bmp = null;
        try {
            URL url = new URL(urlString);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new TileData(BitmapUtils.createBitmapFromAndroidBitmap(bmp).compressToInternal());
	}
	
}
