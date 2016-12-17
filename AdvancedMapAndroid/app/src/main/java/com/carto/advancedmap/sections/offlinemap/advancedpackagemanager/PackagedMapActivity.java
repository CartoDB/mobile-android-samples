package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.os.Bundle;

import com.carto.advancedmap.shared.activities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOfflineVectorTileLayer;
import com.carto.packagemanager.CartoPackageManager;

/**
 * A uses AdvancedPackageManagerActivity datasource. This has maps which are downloaded offline using PackageManager
 */
@ActivityData(name = "Packaged Map", description = "This has maps which are downloaded offline using PackageManager")
public class PackagedMapActivity extends MapBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        // Create style set
        CartoPackageManager manager = AdvancedPackageManagerActivity.Manager;

        CartoOfflineVectorTileLayer layer = new CartoOfflineVectorTileLayer(manager, CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(layer);

        ActivityData data = ((ActivityData) this.getClass().getAnnotations()[0]);
        String name = data.name();
        String description = data.description();

        setTitle(name);
        getActionBar().setSubtitle(description);
    }

}
