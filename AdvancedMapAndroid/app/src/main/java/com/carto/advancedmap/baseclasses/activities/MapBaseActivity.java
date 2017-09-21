package com.carto.advancedmap.baseclasses.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.carto.advancedmap.R;
import com.carto.advancedmap.baseclasses.views.MapBaseView;
import com.carto.advancedmap.main.MainActivity;
import com.carto.advancedmap.main.MainView;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

/**
 * Base activity for map samples. Includes simple lifecycle management
 */
public class MapBaseActivity extends BaseActivity {

    protected MapBaseView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra(MainActivity.TITLE);
        String description = getIntent().getStringExtra(MainActivity.DESCRIPTION);

        setTitle(title);
        getActionBar().setSubtitle(description);

        contentView = new MapBaseView(this);
        setContentView(contentView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
