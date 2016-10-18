package com.carto.cartomap.sections;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ZoomControls;

import com.carto.cartomap.R;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

/**
 * Base activity for map samples. Includes simple lifecycle management
 */
public class BaseMapActivity extends Activity {

    protected MapView mapView;
    protected Projection baseProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create map view
        setContentView(R.layout.activity_main);
        mapView = (MapView) this.findViewById(R.id.map_view);

        baseProjection = mapView.getOptions().getBaseProjection();

        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(layer);

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
