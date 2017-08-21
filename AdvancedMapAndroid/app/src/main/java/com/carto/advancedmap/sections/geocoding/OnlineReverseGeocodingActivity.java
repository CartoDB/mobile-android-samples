package com.carto.advancedmap.sections.geocoding;

import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;
import com.carto.geocoding.PeliasOnlineReverseGeocodingService;

/**
 * Created by aareundo on 21/08/2017.
 */
@ActivityData(name = "Online Reverse Geocoding", description = "Online geocoding with Pelias geocoder")
public class OnlineReverseGeocodingActivity extends BaseReverseGeocodingActivity {

    @Override
    public String getFailMessage() {
        return "Reverse geocoding failed. Please try again";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = new PeliasOnlineReverseGeocodingService(API_KEY);
    }
}
