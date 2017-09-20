package com.carto.advancedmap.baseclasses.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.carto.advancedmap.R;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.TileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

/**
 * Base activity for map samples. Includes simple lifecycle management
 */
public class MapBaseActivity extends BaseActivity {

    public MapView mapView;
    protected Projection baseProjection;
    protected TileLayer baseLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create map view 
        setContentView(R.layout.activity_main);
        mapView = (MapView) this.findViewById(R.id.map_view);
        // Set projection
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
    	mapView.onPause(); 
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	mapView.onResume();
    }

    protected void addBaseLayer(CartoBaseMapStyle style)
    {
        CartoOnlineVectorTileLayer base = new CartoOnlineVectorTileLayer(style);
        mapView.getLayers().add(base);
    }
}
