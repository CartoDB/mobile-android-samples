package com.carto.advancedmap.sections.geocoding.offline;

import android.content.Intent;
import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.sections.geocoding.base.BaseGeoPackageDownloadActivity;

/**
 * Created by aareundo on 21/08/2017.
 */

@ActivityData(name = "Offline Reverse Geocoding", description = "Download offline geocoding packages")
public class ReverseGeoPackageDownloadActivity extends BaseGeoPackageDownloadActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapIconClick() {
        super.onMapIconClick();

        Intent myIntent = new Intent(this, OfflineReverseGeocodingBaseActivity.class);
        startActivity(myIntent);
    }
}
