package com.carto.advancedmap.sections.geocoding.online;

import android.os.Bundle;

import com.carto.advancedmap.sections.geocoding.base.ReverseGeocodingBaseActivity;
import com.carto.advancedmap.utils.Sources;
import com.carto.core.MapPos;
import com.carto.geocoding.MapBoxOnlineReverseGeocodingService;

/**
 * Created by aareundo on 21/08/2017.
 */

public class OnlineReverseGeocodingActivity extends ReverseGeocodingBaseActivity {

    @Override
    public String getFailMessage() {
        return "Reverse geocoding failed. Please try again";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = new MapBoxOnlineReverseGeocodingService(Sources.MAPBOX_TOKEN);

        contentView.removeButton(contentView.downloadButton);

        // Zoom in to Washington
        MapPos pos = contentView.projection.fromWgs84(new MapPos(-77.004590, 38.888702));
        contentView.mapView.setFocusPos(pos, 0);
        contentView.mapView.setZoom(16.0f, 0);
    }
}
