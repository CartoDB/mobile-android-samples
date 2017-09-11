package com.carto.advancedmap.shared.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import com.carto.advancedmap.R;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.TileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base activity for map samples. Includes simple lifecycle management
 */
public class MapBaseActivity extends BaseActivity {

    public MapView mapView;
    protected Projection baseProjection;
    protected TileLayer baseLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create map view 
        setContentView(R.layout.activity_main);
        mapView = (MapView) this.findViewById(R.id.map_view);
        // Set projection
        baseProjection = mapView.getOptions().getBaseProjection();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        setTitle(title);
        getActionBar().setSubtitle(description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
    	mapView.onPause(); 
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	mapView.onResume();
    }

    protected void addBaseLayer(CartoBaseMapStyle style)
    {
        CartoOnlineVectorTileLayer base = new CartoOnlineVectorTileLayer(style);
        mapView.getLayers().add(base);
    }

    public static void takeScreenshot(String name, Activity activity)
    {

        // In Testdroid Cloud, taken screenshots are always stored
        // under /test-screenshots/ folder and this ensures those screenshots
        // be shown under Test Results
        String path =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/test-screenshots/" + name + ".png";

        View scrView = activity.getWindow().getDecorView().getRootView();
        scrView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(scrView.getDrawingCache());
        scrView.setDrawingCacheEnabled(false);

        OutputStream out = null;
        File imageFile = new File(path);

        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
        } catch (FileNotFoundException e) {
            // exception
        } catch (IOException e) {
            // exception
        } finally {

            try {
                if (out != null) {
                    out.close();
                }

            } catch (Exception exc) {
            }

        }
    }
}
