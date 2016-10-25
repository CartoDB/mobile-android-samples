package com.carto.advancedmap.sections.offlinemap;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.carto.advancedmap.MapApplication;
import com.carto.advancedmap.list.Description;
import com.carto.advancedmap.baseactivities.VectorMapSampleBaseActivity;
import com.carto.core.MapRange;
import com.carto.datasources.MBTilesTileDataSource;
import com.carto.datasources.TileDataSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A sample that uses bundled asset for offline base map.
 * As MBTilesDataSource can be used only with files residing in file system,
 * the assets needs to be copied first to the SDCard.
 */
@Description(value = "Bundle MBTiles file for offline base map")
public class BundledMBTilesActivity extends VectorMapSampleBaseActivity {

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

        // Offline map data source
        String mbTileFile = "world_zoom5.mbtiles";

        try {
            String localDir = getExternalFilesDir(null).toString();
            copyAssetToSDCard(getAssets(), mbTileFile, localDir);

            String path = localDir + "/" + mbTileFile;
            Log.i(MapApplication.LOG_TAG,"copy done to " + path);
            MBTilesTileDataSource vectorTileDataSource = new MBTilesTileDataSource(0, 4, path);

            return vectorTileDataSource;

        } catch (IOException e) {
            Log.e(MapApplication.LOG_TAG, "mbTileFile cannot be copied: "+mbTileFile);
            Log.e(MapApplication.LOG_TAG, e.getLocalizedMessage());
        }

    	return null;
    }

    public void copyAssetToSDCard(AssetManager assetManager, String fileName, String toDir) throws IOException {

        InputStream in = assetManager.open(fileName);
        File outFile = new File(toDir, fileName);
        // TODO jaak - check if storage is available and has enough space
        if(outFile.exists()){
            Log.d(MapApplication.LOG_TAG, "file already exits: "+outFile.getAbsolutePath());
            return;
        }

        OutputStream out = new FileOutputStream(outFile);
        copyFile(in, out);
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
