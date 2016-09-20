package com.carto.advancedmap.map;

import android.os.Bundle;
import android.util.Log;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.advancedmap.util.AssetCopy;
import com.carto.advancedmap.util.Const;
import com.carto.core.MapRange;
import com.carto.datasources.MBTilesTileDataSource;
import com.carto.datasources.TileDataSource;

import java.io.IOException;

/**
 * A sample that uses bundled asset for offline base map.
 * As MBTilesDataSource can be used only with files residing in file system,
 * the assets needs to be copied first to the SDCard.
 */
@Description(value = "Bundle MBTiles file for offline base map")
public class OfflineVectorMapActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);
        
        // Limit zoom range, as we have tiles only up to level 5
        mapView.getOptions().setZoomRange(new MapRange(0,6));
        mapView.setZoom(3, 0);
    }
    
    @Override
    protected TileDataSource createTileDataSource() {
        // offline map data source
        String mbTileFile = "world_zoom5.mbtiles";

        try {
            String localDir = getExternalFilesDir(null).toString();
            AssetCopy.copyAssetToSDCard(getAssets(), mbTileFile, localDir);
            Log.i(Const.LOG_TAG,"copy done to " + localDir + "/"
                    + mbTileFile);
            MBTilesTileDataSource vectorTileDataSource = new MBTilesTileDataSource(0, 4, localDir + "/"
                    + mbTileFile);
            return vectorTileDataSource;            
        } catch (IOException e) {
            Log.e(Const.LOG_TAG, "mbTileFile cannot be copied: "+mbTileFile);
            Log.e(Const.LOG_TAG, e.getLocalizedMessage());
        }
    	return null;
    }
}
