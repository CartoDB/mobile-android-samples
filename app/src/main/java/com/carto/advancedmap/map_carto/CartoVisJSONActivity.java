package com.carto.advancedmap.map_carto;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.carto.advancedmap.map_base.MapSampleBaseActivity;
import com.carto.advancedmap.util.Const;
import com.carto.core.MapPos;
import com.carto.core.Variant;
import com.carto.core.VariantType;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.Layer;
import com.carto.layers.TileLayer;
import com.carto.layers.UTFGridEventListener;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.services.CartoVisBuilder;
import com.carto.services.CartoVisLoader;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.UTFGridClickInfo;
import com.carto.vectorelements.BalloonPopup;

import java.io.IOException;

/**
 * A sample demonstrating how to use high level Carto VisJSON API to display interactive maps.
 * A list of different visjson URLs can be selected from the menu.
 * CartoVisLoader class is used to load and configure all corresponding layers.
 * Items on overlay layers are clickable, this is implemented using custom UTFGridEventListener.
 */
public class CartoVisJSONActivity extends MapSampleBaseActivity {

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

            // Check if the layer has info window. In that case will add a custom UTF grid event listener to the layer.
            Variant infoWindow = attributes.getObjectElement("infowindow");
            if (infoWindow.getType() == VariantType.VARIANT_TYPE_OBJECT) {
                MyUTFGridEventListener myEventListener = new MyUTFGridEventListener(vectorLayer, infoWindow);
                TileLayer tileLayer = (TileLayer) layer;
                tileLayer.setUTFGridEventListener(myEventListener);
            }
        }
    }

    private static class MyUTFGridEventListener extends UTFGridEventListener {
        private VectorLayer vectorLayer;
        private Variant infoWindow;

        public MyUTFGridEventListener(VectorLayer vectorLayer, Variant infoWindow) {
            this.vectorLayer = vectorLayer;
            this.infoWindow = infoWindow;
        }

        @Override
        public boolean onUTFGridClicked(UTFGridClickInfo utfGridClickInfo) {
            LocalVectorDataSource vectorDataSource = (LocalVectorDataSource) vectorLayer.getDataSource();

            // Clear previous popups
            vectorDataSource.clear();

            // Multiple vector elements can be clicked at the same time, we only care about the one
            // Check the type of vector element
            BalloonPopup clickPopup = null;
            BalloonPopupStyleBuilder styleBuilder = new BalloonPopupStyleBuilder();
            // Configure style
            styleBuilder.setLeftMargins(new BalloonPopupMargins(0, 0, 0, 0));
            styleBuilder.setTitleMargins(new BalloonPopupMargins(6, 3, 6, 3));
            // Make sure this label is shown on top all other labels
            styleBuilder.setPlacementPriority(10);

            // Show clicked element variant as JSON string
            String desc = utfGridClickInfo.getElementInfo().toString();

            clickPopup = new BalloonPopup(utfGridClickInfo.getClickPos(),
                    styleBuilder.buildStyle(),
                    "Clicked",
                    desc);
            vectorDataSource.add(clickPopup);
            return true;
        }
    }

    private String visJSONURL = "http://documentation.cartodb.com/api/v2/viz/836e37ca-085a-11e4-8834-0edbca4b5057/viz.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        // Load the initial visJSON
        updateVis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addVisJSONMenuOption(menu, "circle", "http://documentation.cartodb.com/api/v2/viz/836e37ca-085a-11e4-8834-0edbca4b5057/viz.json");
        addVisJSONMenuOption(menu, "test", "http://documentation.cartodb.com/api/v2/viz/3ec995a8-b6ae-11e4-849e-0e4fddd5de28/viz.json");
        addVisJSONMenuOption(menu, "countries", "http://documentation.cartodb.com/api/v2/viz/2b13c956-e7c1-11e2-806b-5404a6a683d5/viz.json");
        addVisJSONMenuOption(menu, "dots", "https://documentation.cartodb.com/api/v2/viz/236085de-ea08-11e2-958c-5404a6a683d5/viz.json");

        return true;
    }

    private void addVisJSONMenuOption(final Menu menu, String text, final String value) {
        MenuItem menuItem = menu.add(text).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick (MenuItem item){
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem otherItem = menu.getItem(i);
                    if (otherItem == item) {
                        otherItem.setIcon(android.R.drawable.checkbox_on_background);
                    } else {
                        otherItem.setIcon(null);
                    }
                }
                visJSONURL = value;
                updateVis();
                return true;
            }
        });
        if (visJSONURL.equals(value)) {
            menuItem.setIcon(android.R.drawable.checkbox_on_background);
        }
    }

    protected void updateVis() {
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
                MyCartoVisBuilder visBuilder = new MyCartoVisBuilder(vectorLayer);
                try {
                    loader.loadVis(visBuilder, visJSONURL);
                }
                catch (IOException e) {
                    Log.e(Const.LOG_TAG, "Exception: " + e);
                }

                // Add the created popup overlay layer on top of all visJSON layers
                mapView.getLayers().add(vectorLayer);
            }
        });
        thread.start(); // TODO: should serialize execution
    }
}
