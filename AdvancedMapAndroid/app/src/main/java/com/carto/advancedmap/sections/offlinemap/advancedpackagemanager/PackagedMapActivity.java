package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.os.Bundle;

import com.carto.advancedmap.baseactivities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.datasources.TileDataSource;

/**
 * A uses AdvancedPackageManagerActivity datasource. This has maps which are downloaded offline using PackageManager
 */
@ActivityData(name = "", description = "This has maps which are downloaded offline using PackageManager")
public class PackagedMapActivity extends MapBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);
        mapView.setZoom(3, 0);
    }

    protected TileDataSource createTileDataSource() {
        // Using static global variable to pass data. Bad style, avoid this pattern in your apps     
        if (AdvancedPackageManagerActivity.dataSource != null){
            return AdvancedPackageManagerActivity.dataSource;
        }
        
        return null;
    }

}
