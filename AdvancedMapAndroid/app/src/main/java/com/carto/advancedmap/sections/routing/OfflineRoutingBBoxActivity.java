
package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.main.ActivityData;

import com.carto.advancedmap.shared.activities.BoundingBoxActivity;
import com.carto.advancedmap.utils.BoundingBox;
import com.carto.advancedmap.utils.RouteCalculator;
import com.carto.advancedmap.utils.Sources;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.routing.PackageManagerValhallaRoutingService;
import com.carto.routing.RoutingService;

import java.io.IOException;

/**
 * Created by aareundo on 14/03/17.
 */

@ActivityData(name = "Offline Routing (bbox)", description = "Offline Routing where a bounding box of New York is downloaded")
public class OfflineRoutingBBoxActivity extends BoundingBoxActivity {

    RouteCalculator calculator;
    RoutingService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        contentView.setOnlineBaseLayer();

        service = new PackageManagerValhallaRoutingService(manager);

        calculator = new RouteCalculator(this, contentView.mapView, service);
    }

    @Override
    protected String createPackageFolder() {
        return createPackageFolder("cityroutingpackages");
    }

    @Override
    protected CartoPackageManager getPackageManager(String folder) {

        try {
            String source = Sources.ROUTING_TAG + Sources.OFFLINE_ROUTING;
            return new CartoPackageManager(source, folder);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected BoundingBox getBoundingBox() {
        // New York
        return new BoundingBox(-74.1205, 40.4621, -73.4768, 41.0043);
    }
}