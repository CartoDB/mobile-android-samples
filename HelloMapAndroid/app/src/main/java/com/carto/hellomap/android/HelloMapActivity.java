package com.carto.hellomap.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.carto.components.RenderProjectionMode;
import com.carto.core.MapPos;
import com.carto.datasources.LocalVectorDataSource;

import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.VectorLayer;
import com.carto.projections.EPSG4326;
import com.carto.projections.Projection;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.MapView;
import com.carto.vectorelements.Marker;

import java.util.Random;

public class HelloMapActivity extends Activity {

    static final String LICENSE = "XTUMwQ0ZDNGo4cFZKMklMZHlFQVdZditGYzduazV4QzZBaFVBbkJzRUExMmhqVnFxSEY3bkpTUFVyM0M2NzdRPQoKYXBwVG9rZW49YzQxYTM5ZjktN2I5MC00MThhLTkyZjUtN2I0ODljZDYxZmFhCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5oZWxsb21hcC5hbmRyb2lkCm9ubGluZUxpY2Vuc2U9MQpwcm9kdWN0cz1zZGstYW5kcm9pZC00LioKd2F0ZXJtYXJrPWNhcnRvZGIK";

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapView.registerLicense(LICENSE, getApplicationContext());

        // Set view from layout resource
        setContentView(R.layout.activity_hello_map);
        setTitle("Hello Map");
        mapView = (MapView) this.findViewById(R.id.map_view);

        // Set map view options
        Projection proj = new EPSG4326();
        mapView.getOptions().setBaseProjection(proj);
        mapView.getOptions().setRenderProjectionMode(RenderProjectionMode.RENDER_PROJECTION_MODE_SPHERICAL);
        mapView.getOptions().setZoomGestures(true);
        mapView.getOptions().setClearColor(new com.carto.graphics.Color(0xff000000));

        // Add base map
        CartoOnlineVectorTileLayer baseLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);
        mapView.getLayers().add(baseLayer);

        // Set default location and zoom
        MapPos berlin = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(13.38933, 52.51704));
        mapView.setFocusPos(berlin, 0);
        mapView.setZoom(2, 0);
        mapView.setZoom(4, 2);

        // Create data source and layer for makrers
        LocalVectorDataSource dataSource = new LocalVectorDataSource(proj);
        VectorLayer layer = new VectorLayer(dataSource);
        mapView.getLayers().add(layer);

        // Build Marker style
        MarkerStyleBuilder styleBuilder = new MarkerStyleBuilder();
        styleBuilder.setSize(20);
        styleBuilder.setColor(new com.carto.graphics.Color(Color.WHITE));
        MarkerStyle style = styleBuilder.buildStyle();

        // Create the actual Marker and add it to the data source
        Marker marker = new Marker(berlin, style);
        dataSource.add(marker);

        // Set map event listener to receive click events
        mapView.setMapEventListener(new MyMapEventListener(dataSource));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Disconnect map event listener to avoid leaks
        mapView.setMapEventListener(null);
    }

    /*********************
        MAP CLICK LISTENER
    **********************/
    private static class MyMapEventListener extends MapEventListener {

        private int[] colors = { Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN };

        private LocalVectorDataSource dataSource;

        private Random random;

        public MyMapEventListener(LocalVectorDataSource dataSource) {
            this.dataSource = dataSource;
            this.random = new Random();
        }

        @Override
        public void onMapClicked(MapClickInfo mapClickInfo) {
            super.onMapClicked(mapClickInfo);

            // Build new marker style with random size and color
            MarkerStyleBuilder styleBuilder = new MarkerStyleBuilder();
            styleBuilder.setSize(random.nextInt(30) + 15);
            styleBuilder.setColor(new com.carto.graphics.Color(colors[random.nextInt(colors.length)]));
            MarkerStyle style = styleBuilder.buildStyle();

            // Create a new marker with the defined style at the clicked position
            Marker marker = new Marker(mapClickInfo.getClickPos(), style);
            dataSource.add(marker);
        }
    }
}
