package com.carto.cartomap.sections.torqueapi;

import android.os.Bundle;
import android.util.Log;

import com.carto.cartomap.sections.BaseMapActivity;
import com.carto.cartomap.util.ActivityData;
import com.carto.core.MapPos;
import com.carto.core.StringVariantMap;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.PersistentCacheTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.Layer;
import com.carto.layers.LayerVector;
import com.carto.layers.TorqueTileLayer;
import com.carto.services.CartoMapsService;
import com.carto.styles.CartoCSSStyleSet;
import com.carto.vectortiles.TorqueTileDecoder;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A sample demonstrating how to use Carto Torque tiles with CartoCSS styling
 */
@ActivityData(name = "Torque Ship", description = "Shows indoor movement on a cruise ship throughout the day")
public class TorqueShipActivity extends BaseMapActivity {

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private static final long FRAME_TIME_MS = 100;

    boolean stopped;

    TorqueTileDecoder decoder;
    TorqueTileLayer torqueLayer;

    TorqueTileLayer getTorqueLayer()
    {
            if (torqueLayer != null)
            {
                return torqueLayer;
            }

            for (int i = 0; i < mapView.getLayers().count(); i++)
            {
                Layer layer = mapView.getLayers().get(i);
                if (layer instanceof TorqueTileLayer)
                {
                    torqueLayer = (TorqueTileLayer)layer;
                    decoder = (TorqueTileDecoder)torqueLayer.getTileDecoder();
                    return torqueLayer;
                }
            }

            return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // BaseMapActivity creates and sets mapView
        super.onCreate(savedInstanceState);

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY);

        final String username = "solutions";
        final String mapname = "tpl_a108ee2b_6699_43bc_aa71_3b0bc962acf9";
        boolean isVector = false;

        final CartoMapsService service = new CartoMapsService();
        service.setUsername(username);

        service.setDefaultVectorLayerMode(isVector);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LayerVector layers = null;
                try {
                    layers = service.buildNamedMap(mapname, new StringVariantMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // NB! This update priority only works for the map tpl_a108ee2b_6699_43bc_aa71_3b0bc962acf9
                // It may make loading worse or even break it when tried with other maps
                layers.get(0).setUpdatePriority(2);
                layers.get(1).setUpdatePriority(1);
                layers.get(2).setUpdatePriority(0);

                for (int i = 0; i < layers.size(); i++) {
                    Layer layer = layers.get(i);
                    mapView.getLayers().add(layer);
                }

                task.run();

            }
        });

        thread.start();

        MapPos center = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(0.0013, 0.0013));
        mapView.setFocusPos(center, 0);
        mapView.setZoom(18.0f, 0);
    }


    @Override
    protected void onStart() {
        synchronized (worker) {
            stopped = false;
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        synchronized (worker) {
            stopped = true;
        }

        super.onStop();
    }

    private Runnable task = new Runnable() {
        public void run() {
            synchronized (worker) {

                if (getTorqueLayer() == null) {
                    // getTorqueLayer() function initializes torqueLayer and decoder
                    return;
                }

                int frameCount = decoder.getFrameCount();
                int frameNr = (torqueLayer.getFrameNr() + 1) % frameCount;

                torqueLayer.setFrameNr(frameNr);

                if (!stopped) {
                    worker.schedule(task, FRAME_TIME_MS, TimeUnit.MILLISECONDS);
                }
            }
        }
    };

}
