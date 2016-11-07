package com.carto.advancedmap.sections.offlinemap.packagemanager;

import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.baseactivities.VectorMapSampleBaseActivity;
import com.carto.datasources.TileDataSource;

/**
 * A uses PackageManagerActivity datasource. This has maps which are downloaded offline using PackageManager
 */
@ActivityData(name = "", description = "This has maps which are downloaded offline using PackageManager")
public class PackagedMapActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);
        mapView.setZoom(3, 0);
    }
    
    @Override
    protected TileDataSource createTileDataSource() {
        // Using static global variable to pass data. Bad style, avoid this pattern in your apps     
        if (PackageManagerActivity.dataSource != null){
            return PackageManagerActivity.dataSource;
        }
        
        return super.createTileDataSource();
    }

}
