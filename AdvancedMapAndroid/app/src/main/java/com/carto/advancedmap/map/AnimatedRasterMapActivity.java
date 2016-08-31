package com.carto.advancedmap.map;

import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.map_base.VectorMapSampleBaseActivity;
import com.carto.advancedmap.datasource.MyAnimatedTileDataSource;
import com.carto.datasources.MemoryCacheTileDataSource;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.RasterTileLayer;
import com.carto.layers.TileLoadListener;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A sample demonstrating how to add animated raster overlay on top of vector base map.
 * The sample uses custom tile data source for downloading different tiles for different
 * animation frames and shows how to use TileLoadListener.
 */
@Description(value = "Animated raster tile overlay")
public class AnimatedRasterMapActivity extends VectorMapSampleBaseActivity {

    private RasterTileLayer animatedRasterTileLayer;
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private static final int ANIMATION_FRAME_TIME_MS = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);
        
        mapView.setZoom(6, 0);
        
        // Animated raster tile datasource
        final int[] hours = new int[]{5, 7, 9, 11, 15, 19, 23, 27};
        ArrayList<TileDataSource> animatedRasterTileDataSources = new ArrayList<TileDataSource>();
        for (int hour : hours) {
            MemoryCacheTileDataSource dataSource = new MemoryCacheTileDataSource(
                    new HTTPTileDataSource(4, 7, "http://www.openportguide.org/tiles/actual/wind_vector/" + hour + "/{zoom}/{x}/{y}.png"));
            // Reduce the size a bit (default is 6 mb)
            dataSource.setCapacity((long) (0.5f * 1024 * 1024));
            animatedRasterTileDataSources.add(dataSource);
        }
        MyAnimatedTileDataSource animatedRasterTileDataSource = new MyAnimatedTileDataSource(0, 24, animatedRasterTileDataSources);
        
        // Initialize an animated raster layer
        animatedRasterTileLayer = new RasterTileLayer( animatedRasterTileDataSource);
        animatedRasterTileLayer.setSynchronizedRefresh(true);
        animatedRasterTileLayer.setPreloading(false);
        // Set the tile load listener, which will be used to change the animation frames
        animatedRasterTileLayer.setTileLoadListener(new TileLoadListener() {
            private boolean inProgress;
            
            @Override
            public void onVisibleTilesLoaded() {
                // All visible tiles have been loaded, change the frame 
                Runnable task = new Runnable() {
                    public void run() {
                        synchronized (worker) {
                            inProgress = false;
                            animatedRasterTileLayer.setFrameNr((animatedRasterTileLayer.getFrameNr() + 1) % hours.length);
                        }
                    }
                };
                synchronized (worker) {
                    if (!inProgress) {
                        inProgress = true;
                        worker.schedule(task, ANIMATION_FRAME_TIME_MS, TimeUnit.MILLISECONDS);
                    }
                }
            }
            
            @Override
            public void onPreloadingTilesLoaded() {
            	
            }
        });

        // Add the previous raster layer to the map
        mapView.getLayers().add(animatedRasterTileLayer);
    }
    
    @Override
    public void onDestroy() {
    	animatedRasterTileLayer.setTileLoadListener(null);
    	super.onDestroy();
    }
}
