package com.carto.advancedmap.sections.geocoding.online;

import android.os.Bundle;

import com.carto.advancedmap.sections.geocoding.base.GeocodingBaseActivity;
import com.carto.advancedmap.utils.Sources;
import com.carto.geocoding.PeliasOnlineGeocodingService;

/**
 * Created by aareundo on 21/08/2017.
 */

public class OnlineGeocodingActivity extends GeocodingBaseActivity {

    @Override
    public String getFailMessage() {
        return "Reverse geocoding failed. Please try again";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = new PeliasOnlineGeocodingService(Sources.API_KEY);

        contentView.removeButton(contentView.downloadButton);
    }
}
