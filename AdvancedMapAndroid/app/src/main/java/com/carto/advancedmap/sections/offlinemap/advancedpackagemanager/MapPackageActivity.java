package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.os.Bundle;

import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.main.ActivityData;
import com.carto.layers.CartoOfflineVectorTileLayer;
import com.carto.packagemanager.CartoPackageManager;

/**
 * A uses OfflineMapActivity datasource. This has maps which are downloaded offline using PackageManager
 */
@ActivityData(name = "Packaged Map", description = "This has maps which are downloaded offline using PackageManager")
public class MapPackageActivity extends MapBaseActivity {

    public static CartoPackageManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        ActivityData data = ((ActivityData) this.getClass().getAnnotations()[0]);
        String name = data.name();
        String description = data.description();

        setTitle(name);
        getActionBar().setSubtitle(description);

        CartoOfflineVectorTileLayer layer = new CartoOfflineVectorTileLayer(manager, BaseMapsView.DEFAULT_STYLE);
        contentView.mapView.getLayers().add(layer);
    }

}
