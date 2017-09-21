package com.carto.advancedmap.sections.geocoding.offline;

import android.content.Intent;
import android.os.Bundle;

import com.carto.advancedmap.main.ActivityData;
import com.carto.advancedmap.sections.geocoding.base.BaseGeoPackageDownloadActivity;

/**
 * Created by aareundo on 21/08/2017.
 */

public class GeoPackageDownloadActivity extends BaseGeoPackageDownloadActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapIconClick() {
        super.onMapIconClick();

        Intent myIntent = new Intent(this, OfflineGeocodingActivity.class);
        startActivity(myIntent);
    }
}
