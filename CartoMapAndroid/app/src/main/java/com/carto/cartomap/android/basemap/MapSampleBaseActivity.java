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
    protected Projection baseProjection;
    protected TileLayer baseLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Basic map setup
        // Create map view
        setContentView(R.layout.activity_main);
        mapView = (MapView) this.findViewById(R.id.map_view);

        // Set the base projection, that will be used for most MapView, MapEventListener and Options methods
        baseProjection = new EPSG3857();
        mapView.getOptions().setBaseProjection(baseProjection);

        // General options
        mapView.getOptions().setTileDrawSize(256);
//        mapView.getOptions().setTileThreadPoolSize(4);

        // Review following and change if needed
        mapView.getOptions().setRotatable(true);
        mapView.getOptions().setZoomRange(new MapRange(0, 18));

        Log.d("EXCEPTION", "autoconfigured DPI="+mapView.getOptions().getDPI());

        // Set default location
//      mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(24.650415, 59.420773)), 0); // tallinn
//      mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(19.04468, 47.4965)), 0); // budapest
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(13.38933, 52.51704)), 0); // berlin
        mapView.setZoom(2, 0);
        mapView.setMapRotation(0, 0);
        mapView.setTilt(90, 0);

        // Add listeners to zoom controls
        ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoom_controls);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.zoom(1.0f, 0.3f); // zoom-in exactly one level, duration 0.3 secs
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
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        MapPos focusPos = new MapPos(bundle.getDouble("focusX"), bundle.getDouble("focusY"));
        mapView.setFocusPos(focusPos, 0);
        mapView.setZoom(bundle.getFloat("zoom"), 0);
        mapView.setMapRotation(bundle.getFloat("rotation"), 0);
        mapView.setTilt(bundle.getFloat("tilt"), 0);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        MapPos focusPos = mapView.getFocusPos();
        bundle.putDouble("focusX", focusPos.getX());
        bundle.putDouble("focusY", focusPos.getY());
        bundle.putFloat("zoom", mapView.getZoom());
        bundle.putFloat("rotation", mapView.getMapRotation());
        bundle.putFloat("tilt", mapView.getTilt());
    }
}
