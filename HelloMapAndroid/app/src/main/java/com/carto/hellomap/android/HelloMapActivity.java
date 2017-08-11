package com.carto.hellomap.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.carto.core.MapPos;
import com.carto.datasources.LocalVectorDataSource;

import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.VectorLayer;
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
        mapView = (MapView) this.findViewById(R.id.map_view);

        // Add base map
        CartoOnlineVectorTileLayer baseLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(baseLayer);

        // Set default location and zoom
        MapPos berlin = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(13.38933, 52.51704));
        mapView.setFocusPos(berlin, 0);
        mapView.setZoom(10, 0);

        Marker marker = addMarkerToPosition(berlin);

        mapView.setMapEventListener(new MyMapEventListener(marker));

        setTitle("Hello Map");
    }

    private Marker addMarkerToPosition(MapPos position)
    {
        // Create a new layer and add it to the map
        Projection projection = mapView.getOptions().getBaseProjection();
        LocalVectorDataSource datasource = new LocalVectorDataSource(projection);
        VectorLayer layer = new VectorLayer(datasource);
        mapView.getLayers().add(layer);

        // Build Marker style
        MarkerStyleBuilder builder = new MarkerStyleBuilder();
        builder.setSize(20);

        // CARTO has its own Color class. Do not mix up with android.graphics.Color
        builder.setColor(new com.carto.graphics.Color(Color.WHITE));

        MarkerStyle style = builder.buildStyle();

        // Create the actual Marker and add it to the data source
        Marker marker = new Marker(position, style);
        datasource.add(marker);

        return marker;
    }

    /*********************
        MAP CLICK LISTENER
    **********************/
    private class MyMapEventListener extends MapEventListener {

        private int[] colors = { Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN };

        private Marker marker;

        private Random random;

        public MyMapEventListener(Marker marker) {
            this.marker = marker;
            random = new Random();
        }

        @Override
        public void onMapClicked(MapClickInfo mapClickInfo) {
            super.onMapClicked(mapClickInfo);

            MarkerStyleBuilder builder = new MarkerStyleBuilder();

            // Set random size (within reasonable limits)
            int size = getRandomInt(15, 50);
            builder.setSize(size);

            // Set random color from our list
            int color = colors[getRandomInt(0, colors.length)];
            builder.setColor(new com.carto.graphics.Color(color));

            // Set a new style to the marker
            marker.setStyle(builder.buildStyle());
        }

        private int getRandomInt(int min, int max) {
            return random.nextInt(max - min) + min;
        }
    }
}
