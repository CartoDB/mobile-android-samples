
package com.carto.advancedmap.sections.routing;

import com.carto.advancedmap.list.ActivityData;

import com.carto.advancedmap.shared.activities.BoundingBoxActivity;
import com.carto.advancedmap.utils.BoundingBox;
import com.carto.packagemanager.CartoPackageManager;

import java.io.IOException;

/**
 * Created by aareundo on 14/03/17.
 */

@ActivityData(name = "Offline Routing (bbox)", description = "Offline Routing where a bounding box of New York is downloaded")
public class OfflineRoutingBBoxActivity extends BoundingBoxActivity {

    static final String ROUTING = "routing:";
    static  final String SOURCE = "valhalla.osm";

    @Override
    protected String createPackageFolder() {
        return createPackageFolder("cityroutingpackages");
    }

    @Override
    protected CartoPackageManager getPackageManager(String folder) {

        try {


            String source = ROUTING + SOURCE;
            return new CartoPackageManager(source, folder);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected BoundingBox getBoundingBox() {
        // New York
        return new BoundingBox(-73.4768, 40.4621, -74.1205, 41.0043);
    }
}