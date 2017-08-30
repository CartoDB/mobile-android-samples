package com.carto.advancedmap.sections.geocoding.online;

import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.sections.geocoding.base.BaseGeocodingActivity;
import com.carto.advancedmap.utils.Sources;
import com.carto.geocoding.PeliasOnlineGeocodingService;

/**
 * Created by aareundo on 21/08/2017.
 */
@ActivityData(name = "Online Geocoding", description = "Online geocoding with Pelias geocoder")
public class OnlineGeocodingActivity extends BaseGeocodingActivity {

    @Override
    public String getFailMessage() {
        return "Reverse geocoding failed. Please try again";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = new PeliasOnlineGeocodingService(Sources.API_KEY);
    }
}
