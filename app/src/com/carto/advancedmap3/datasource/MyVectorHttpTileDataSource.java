package com.carto.advancedmap3.datasource;

import com.carto.core.MapTile;
import com.carto.core.TileData;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.utils.Log;
import com.carto.core.BinaryData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

/**
 * A minimal custom vector tile data source which uses app-level HTTP requests
 * It supports e.g. HTTPS
 */
public class MyVectorHttpTileDataSource extends HTTPTileDataSource {

	public MyVectorHttpTileDataSource(int minZoom, int maxZoom, String baseURL) {
		super(minZoom, maxZoom, baseURL);
	}

	public TileData loadTile(MapTile tile) {

		String urlString = super.buildTileURL(getBaseURL(), tile);

        Log.debug("requesting tile: "+urlString);

        try {
            URL url = new URL(urlString);

            InputStream is = url.openConnection().getInputStream();

            byte[] buffer = new byte[4096];
            int n;
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            while ((n = is.read(buffer)) != -1) {
            	baf.append(buffer, 0, n);
            }
            Log.debug("loaded bytes " + baf.length());
            return new TileData(new BinaryData(baf.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
}
