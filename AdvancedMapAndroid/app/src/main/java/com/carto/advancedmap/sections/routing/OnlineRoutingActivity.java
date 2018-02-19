package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.main.ActivityData;
import com.carto.advancedmap.utils.Sources;
import com.carto.routing.CartoOnlineRoutingService;
import com.carto.routing.ValhallaOnlineRoutingService;

/**
 * Created by aareundo on 16/12/16.
 */

public class OnlineRoutingActivity extends BaseRoutingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CartoOnlineRoutingService service = new CartoOnlineRoutingService("nutiteq.osm.car");

        setService(service);

        contentView.removeButton(contentView.downloadButton);
    }
}
