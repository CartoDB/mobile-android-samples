package com.carto.cartomap.sections.cartojsapi;

import android.os.Bundle;
import android.util.Log;

import com.carto.cartomap.sections.BaseMapActivity;
import com.carto.core.BinaryData;
import com.carto.core.MapPos;
import com.carto.core.Variant;
import com.carto.core.VariantType;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.Layer;
import com.carto.layers.TileLayer;
import com.carto.layers.UTFGridEventListener;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.services.CartoVisBuilder;
import com.carto.services.CartoVisLoader;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyle;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.UTFGridClickInfo;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;
import com.carto.vectorelements.BalloonPopup;

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

    private class MyUTFGridEventListener extends UTFGridEventListener {
        private VectorLayer vectorLayer;

        MyUTFGridEventListener(VectorLayer vectorLayer) {
            this.vectorLayer = vectorLayer;
        }

        @Override
        public boolean onUTFGridClicked(UTFGridClickInfo utfGridClickInfo) {
            LocalVectorDataSource vectorDataSource = (LocalVectorDataSource) vectorLayer.getDataSource();

            // Clear previous popups
            vectorDataSource.clear();

            BalloonPopupStyleBuilder styleBuilder = new BalloonPopupStyleBuilder();

            // Configure style
            styleBuilder.setLeftMargins(new BalloonPopupMargins(0, 0, 0, 0));
            styleBuilder.setTitleMargins(new BalloonPopupMargins(6, 3, 6, 3));

            // Make sure this label is shown on top all other labels
            styleBuilder.setPlacementPriority(10);

            // Show clicked element variant as JSON string
            String description = utfGridClickInfo.getElementInfo().toString();

            // Get position and build style
            MapPos position = utfGridClickInfo.getClickPos();
            BalloonPopupStyle style = styleBuilder.buildStyle();

            BalloonPopup clickPopup = new BalloonPopup(position, style, "Clicked", description);
            vectorDataSource.add(clickPopup);

            return true;
        }
    }
}
