package com.carto.cartomap.android.basemap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ZoomControls;

import com.carto.cartomap.android.R;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.layers.TileLayer;
import com.carto.projections.EPSG3857;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

/**
 * Base activity for map samples. Includes simple lifecycle management
 */
public class MapSampleBaseActivity extends Activity {

    protected MapView mapView;
    protected TileLayer baseLayer;
    protected Projection baseProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create map view
        setContentView(R.layout.activity_main);
        mapView = (MapView) this.findViewById(R.id.map_view);

        baseProjection = mapView.getOptions().getBaseProjection();

        // Review following and change if needed
        mapView.getOptions().setRotatable(true);
        mapView.getOptions().setZoomRange(new MapRange(0, 18));

        Log.d("EXCEPTION", "autoconfigured DPI=" + mapView.getOptions().getDPI());

        // Set default location
        Projection projection = mapView.getOptions().getBaseProjection();
        MapPos berlin = projection.fromWgs84(new MapPos(13.38933, 52.51704));
        mapView.setFocusPos(berlin, 0);
        mapView.setZoom(2, 0);

        // Add listeners to zoom controls
        ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoom_controls);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.zoom(1.0f, 0.3f); // zoom in exactly one level, duration 0.3 secs
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.zoom(-1.0f, 0.3f); // zoom out exactly one level, duration 0.3 secs
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
