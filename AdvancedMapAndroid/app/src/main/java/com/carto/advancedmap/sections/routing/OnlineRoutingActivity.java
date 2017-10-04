package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.main.ActivityData;
import com.carto.advancedmap.utils.Sources;
import com.carto.routing.ValhallaOnlineRoutingService;

/**
 * Created by aareundo on 16/12/16.
 */

public class OnlineRoutingActivity extends BaseRoutingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ValhallaOnlineRoutingService service = new ValhallaOnlineRoutingService(Sources.API_KEY);

        // Set auto by default.
        // Other Mapzen costing models: pedestrian etc, cf.
        // cf. https://github.com/valhalla/valhalla-docs/blob/master/api-reference.md
        service.setProfile("auto");

        setService(service);

        contentView.removeButton(contentView.downloadButton);
    }
}
