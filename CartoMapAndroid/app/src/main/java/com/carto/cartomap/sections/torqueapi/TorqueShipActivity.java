package com.carto.cartomap.sections.torqueapi;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.carto.cartomap.sections.torqueapi.histogram.TorqueHistogramInterface;
import com.carto.cartomap.util.ActivityData;
import com.carto.core.MapPos;
import com.carto.core.StringVariantMap;
import com.carto.layers.Layer;
import com.carto.layers.LayerVector;
import com.carto.layers.TorqueTileLayer;
import com.carto.services.CartoMapsService;

import com.carto.vectortiles.TorqueTileDecoder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A sample demonstrating how to use Carto Torque tiles with CartoCSS styling
 */
@ActivityData(name = "Torque", description = "Shopper movement in a mall throughout the day")
public class TorqueShipActivity extends Activity implements TorqueHistogramInterface {

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

            for (int i = 0; i < contentView.MapView.getLayers().count(); i++)
            {
                Layer layer = contentView.MapView.getLayers().get(i);
                if (layer instanceof TorqueTileLayer)
                {
                    torqueLayer = (TorqueTileLayer)layer;
                    decoder = (TorqueTileDecoder)torqueLayer.getTileDecoder();
                    return torqueLayer;
                }
            }

            return null;
    }

    TorqueView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // BaseMapActivity creates and sets mapView
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        contentView = new TorqueView(this);
        setContentView(contentView);

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
                    contentView.MapView.getLayers().add(layer);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Success: " + getTorqueLayer());
                        contentView.InitializeHistogram(decoder.getFrameCount());
                    }
                });

            }
        });

        thread.start();

        MapPos center = contentView.MapView.getOptions().getBaseProjection().fromWgs84(
                new MapPos(0.0013, 0.0013)
        );
        contentView.MapView.setFocusPos(center, 0);
        contentView.MapView.setZoom(18.0f, 0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentView.Dispose();
    }

    public void onHistogramClicked(int frameNumber) {
        contentView.Histogram.Button.pause();
        contentView.Histogram.Counter.Update(frameNumber);
        getTorqueLayer().setFrameNr(frameNumber);
    }

    @Override
    public void onButtonClicked() {
        // Play/pause is handled inside, use this method to invoke other actions
    }

    Timer timer;
    int max;

    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (contentView.Histogram.Button.isPaused()) {
                    return;
                }

                if (getTorqueLayer() == null) {
                    // getTorqueLayer() function initializes torqueLayer and decoder
                    return;
                }

                final int frameCount = decoder.getFrameCount();
                final int frameNr = (torqueLayer.getFrameNr() + 1) % frameCount;

                torqueLayer.setFrameNr(frameNr);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int count = getTorqueLayer().countVisibleFeatures(frameNr);

                        if (count > max) {
                            max = count;
                            contentView.Histogram.UpdateAll(max);
                        } else {
                            contentView.Histogram.UpdateElement(frameNr, count, max);
                        }

                        contentView.Histogram.Counter.Update(frameNr, frameCount);
                    }
                });
            }
        }, 150, 150);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
