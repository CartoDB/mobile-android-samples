package com.carto.advancedmap.sections.geocoding;

import android.content.Intent;
import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;

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

        Intent myIntent = new Intent(this, OfflineReverseGeocodingActivity.class);
        startActivity(myIntent);
    }
}
