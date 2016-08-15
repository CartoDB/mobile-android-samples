package com.carto.advancedmap.map_hello;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.carto.advancedmap.R;
import com.carto.advancedmap.util.Const;
import com.carto.core.BinaryData;
import com.carto.core.MapPos;
import com.carto.core.Variant;
import com.carto.core.VariantType;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Color;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.Layer;
import com.carto.layers.TileLayer;
import com.carto.layers.UTFGridEventListener;
import com.carto.layers.VectorLayer;
import com.carto.projections.EPSG3857;
import com.carto.projections.Projection;
import com.carto.services.CartoVisBuilder;
import com.carto.services.CartoVisLoader;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyle;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.ui.MapView;
import com.carto.ui.UTFGridClickInfo;
import com.carto.utils.AssetPackage;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Marker;

import java.io.IOException;

/**
 * Created by aareundo on 15/08/16.
 */

public class HelloMapActivity extends Activity {

    static final String LICENSE = "XTUN3Q0ZDUmFWcm9pWEUycVN3LzlGeVhEZWZuVnp3RkJBaFE3dHpTSTlDaEFVeW9aWUNQdmc" +
            "wNDdwNitEWEE9PQoKcHJvZHVjdHM9c2RrLWFuZHJvaWQtMy4qCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZ" +
            "HZhbmNlZG1hcAp3YXRlcm1hcms9bnV0aXRlcQpvbmxpbmVMaWNlbnNlPTEKdXNlcktleT1jZmYyMzI1N2ZiN" +
            "TBiYWFmMDY4NDI1Y2NkNmMwYzk4Mgo=";

    private String url = "http://documentation.carto.com/api/v2/viz/2b13c956-e7c1-11e2-806b-5404a6a683d5/viz.json";

    protected MapView mapView;
    protected Projection baseProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register license
        MapView.registerLicense(LICENSE, getApplicationContext());

        // Set our view from our hello_map layout resource
        setContentView(R.layout.hello_map);
        mapView = (MapView) this.findViewById(R.id.hello_map_view);

        baseProjection = new EPSG3857();

        // 2. Add base map
        BinaryData data = AssetUtils.loadAsset("nutibright-v3.zip");
        AssetPackage styleAssets = new ZippedAssetPackage(data);
        CartoOnlineVectorTileLayer baseLayer = new CartoOnlineVectorTileLayer("nutiteq.osm", styleAssets);
        mapView.getLayers().add(baseLayer);

        // 3. Set default location and zoom
        MapPos berlin = baseProjection.fromWgs84(new MapPos(13.38933, 52.51704));
        mapView.setFocusPos(berlin, 0);
        mapView.setZoom(10, 0);

        updateVis(url);
    }

    protected void updateVis(final String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mapView.getLayers().clear();

                // Create overlay layer for popups
                Projection projection = mapView.getOptions().getBaseProjection();
                LocalVectorDataSource dataSource = new LocalVectorDataSource(projection);
                VectorLayer vectorLayer = new VectorLayer(dataSource);

                // Create VIS loader
                CartoVisLoader loader = new CartoVisLoader();
                loader.setDefaultVectorLayerMode(true);
                MyCartoVisBuilder visBuilder = new MyCartoVisBuilder(vectorLayer);

                try {
                    loader.loadVis(visBuilder, url);
                }
                catch (IOException e) {
                    Log.e(Const.LOG_TAG, "Exception: " + e);
                }

                // Add the created popup overlay layer on top of all visJSON layers
                mapView.getLayers().add(vectorLayer);

                // Add the Marker
                MapPos tallinn = new MapPos(24.646469, 59.426939);
                addMarkerToPosition(mapView, tallinn);
            }
        });

        thread.start(); // TODO: should serialize execution
    }
    private void addMarkerToPosition(MapView map, MapPos wgsPosition)
    {
        // Create a new layer
        Projection projection = map.getOptions().getBaseProjection();
        LocalVectorDataSource datasource = new LocalVectorDataSource(projection);
        VectorLayer layer = new VectorLayer(datasource);

        // Add layer to map
        map.getLayers().add(layer);

        MarkerStyleBuilder builder = new MarkerStyleBuilder();
        builder.setSize(30);

        builder.setColor(new Color(android.graphics.Color.GREEN));

        // Set marker position and style
        MapPos position = projection.fromWgs84(wgsPosition);
        MarkerStyle style = builder.buildStyle();

        // Create marker and add it to the source
        Marker marker = new Marker(position, style);
        datasource.add(marker);
    }

    /****************************
        INTERNAL PRIVATE CLASSES
     ****************************/

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

            MapPos position = utfGridClickInfo.getClickPos();
            BalloonPopupStyle style = styleBuilder.buildStyle();
            clickPopup = new BalloonPopup(position, style, "Clicked", desc);

            vectorDataSource.add(clickPopup);

            return true;
        }
    }
}
