package com.carto.cartomap.android.sections.cartojsapi;

import android.util.Log;

import com.carto.cartomap.android.basemap.MapSampleBaseActivity;
import com.carto.cartomap.android.builder.MyCartoVisBuilder;
import com.carto.core.BinaryData;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.services.CartoVisLoader;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

public class VisJsonBaseActivity extends MapSampleBaseActivity {

    protected void updateVis(final String url) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mapView.getLayers().clear();

                // Create overlay layer for popups
                Projection proj = mapView.getOptions().getBaseProjection();
                LocalVectorDataSource dataSource = new LocalVectorDataSource(proj);
                VectorLayer vectorLayer = new VectorLayer(dataSource);

                // Create VIS loader
                CartoVisLoader loader = new CartoVisLoader();
                loader.setDefaultVectorLayerMode(true);

                BinaryData fontData = AssetUtils.loadAsset("carto-fonts.zip");
                loader.setVectorTileAssetPackage(new ZippedAssetPackage(fontData));

                MyCartoVisBuilder visBuilder = new MyCartoVisBuilder(mapView, vectorLayer);

                try {
                    loader.loadVis(visBuilder, url);
                }
                catch (IOException e) {
                    Log.e("EXCEPTION", "Exception: " + e);
                }

                // Add the created popup overlay layer on top of all visJSON layers
                mapView.getLayers().add(vectorLayer);
            }
        });

        thread.start(); // TODO: should serialize execution
    }
}
