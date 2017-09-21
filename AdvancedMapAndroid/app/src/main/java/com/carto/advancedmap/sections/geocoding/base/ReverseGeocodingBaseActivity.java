package com.carto.advancedmap.sections.geocoding.base;

import com.carto.core.MapPos;
import com.carto.geocoding.GeocodingResult;
import com.carto.geocoding.GeocodingResultVector;
import com.carto.geocoding.ReverseGeocodingRequest;
import com.carto.geocoding.ReverseGeocodingService;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;

import java.io.IOException;

/**
 * Created by aareundo on 21/08/2017.
 */

public class ReverseGeocodingBaseActivity extends GeocodingBaseActivity {

    protected ReverseGeocodingService service;

    @Override
    public void onResume() {
        super.onResume();

        contentView.mapView.setMapEventListener(new MapEventListener() {

            @Override
            public void onMapClicked(MapClickInfo mapClickInfo) {

                MapPos location = mapClickInfo.getClickPos();

                ReverseGeocodingRequest request = new ReverseGeocodingRequest(contentView.projection, location);
                float meters = 125.0f;
                request.setSearchRadius(meters);

                // Scan the results list. If we found relatively close point-based match,
                // use this instead of the first result.
                // In case of POIs within buildings, this allows us to hightlight POI instead of the building
                GeocodingResultVector results;
                try {
                    results = service.calculateAddresses(request);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                GeocodingResult result = null;
                int count = (int)results.size();

                if (count > 0) {
                    result = results.get(0);
                }

                for (int i = 0; i < count; i++) {
                    GeocodingResult other = results.get(i);

                    // 0.8f means 125 * (1.0 - 0.9) = 12.5 meters (rank is relative distance)
                    if (other.getRank() > 0.9f) {
                        String name = other.getAddress().getName();
                        // Points of interest usually have names, others just have addresses
                        if (name != null && name != "") {
                            result = other;
                            break;
                        }
                    }
                }

                if (result == null) {
                    alert(getFailMessage());
                    return;
                }

                String title = "";
                String description = result.toString();
                boolean goToPosition = false;

                showResult(result, title, description, goToPosition);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        contentView.mapView.setMapEventListener(null);
    }
}
