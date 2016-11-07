package com.carto.cartomap.sections;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
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

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        setTitle(title);
        getActionBar().setSubtitle(description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    protected void addBaseLayer(CartoBaseMapStyle style)
    {
        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(style);
        mapView.getLayers().add(layer);
    }
}
