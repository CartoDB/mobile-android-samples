package com.carto.cartomap.sections;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.carto.cartomap.R;
import com.carto.cartomap.main.MainActivity;
import com.carto.cartomap.util.Colors;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

/**
 * Base activity for map samples. Includes simple lifecycle management
 */
public class MapBaseActivity extends AppCompatActivity {

    protected MapView mapView;
    protected Projection baseProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable background = new ColorDrawable(Colors.LOCATION_RED);
        getSupportActionBar().setBackgroundDrawable(background);

        // Create map view
        setContentView(R.layout.activity_main);
        mapView = (MapView) this.findViewById(R.id.map_view);

        baseProjection = mapView.getOptions().getBaseProjection();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra(MainActivity.TITLE);
        String description = getIntent().getStringExtra(MainActivity.DESCRIPTION);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(description);
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
