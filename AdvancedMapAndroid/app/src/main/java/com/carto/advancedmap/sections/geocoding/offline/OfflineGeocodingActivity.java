package com.carto.advancedmap.sections.geocoding.offline;

import android.os.Bundle;

import com.carto.advancedmap.main.ActivityData;
import com.carto.advancedmap.sections.geocoding.base.BaseGeocodingActivity;
import com.carto.geocoding.PackageManagerGeocodingService;

/**
 * Created by aareundo on 21/08/2017.
 */

@ActivityData(name = "Offline Reverse Geocoding", description = "Click the map to find an address")
public class OfflineGeocodingActivity extends BaseGeocodingActivity {

    @Override
    public String getFailMessage() {
        return "Couldn't find any addresses. Are you sure you have downloaded the region you're trying to reverse geocode?";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = new PackageManagerGeocodingService(contentView.manager);
    }
}
