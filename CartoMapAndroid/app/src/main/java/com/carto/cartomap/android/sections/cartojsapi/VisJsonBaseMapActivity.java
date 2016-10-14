package com.carto.cartomap.android.sections.cartojsapi;

import android.util.Log;

import com.carto.cartomap.android.sections.BaseMapActivity;
import com.carto.cartomap.android._old.listener.MyUTFGridEventListener;
import com.carto.core.BinaryData;
import com.carto.core.MapPos;
import com.carto.core.Variant;
import com.carto.core.VariantType;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.Layer;
import com.carto.layers.TileLayer;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.services.CartoVisBuilder;
import com.carto.services.CartoVisLoader;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

public class VisJsonBaseMapActivity extends BaseMapActivity {

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

                MyCartoVisBuilder visBuilder = new MyCartoVisBuilder(vectorLayer);

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

        thread.start();
    }

    private class MyCartoVisBuilder extends CartoVisBuilder {

        private VectorLayer vectorLayer; // vector layer for popups

        public MyCartoVisBuilder(VectorLayer vectorLayer) {
            this.vectorLayer = vectorLayer;
        }

        @Override
        public void setCenter(MapPos mapPos) {
            mapView.setFocusPos(mapView.getOptions().getBaseProjection().fromWgs84(mapPos), 1.0f);
        }

        @Override
        public void setZoom(float zoom) {
            mapView.setZoom(zoom, 1.0f);
        }

        @Override
        public void addLayer(Layer layer, Variant attributes) {

            // Add the layer to the map view
            mapView.getLayers().add(layer);

            // Check if the layer has info window.
            // In that case will add a custom UTF grid event listener to the layer.
            Variant infoWindow = attributes.getObjectElement("infowindow");

            if (infoWindow.getType() == VariantType.VARIANT_TYPE_OBJECT) {
                MyUTFGridEventListener myEventListener = new MyUTFGridEventListener(vectorLayer);
                TileLayer tileLayer = (TileLayer) layer;
                tileLayer.setUTFGridEventListener(myEventListener);
            }
        }
    }
}
