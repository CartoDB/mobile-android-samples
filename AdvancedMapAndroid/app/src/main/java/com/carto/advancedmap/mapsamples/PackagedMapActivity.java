package com.carto.advancedmap.mapsamples;

import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.list.PackageManagerActivity;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.datasources.TileDataSource;

/**
 * A uses PackageManagerActivity datasource. This has maps which are downloaded offline using PackageManager
 */
@Description(value = "This has maps which are downloaded offline using PackageManager")
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
