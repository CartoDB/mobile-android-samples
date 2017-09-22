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
        setService(service);

        contentView.removeButton(contentView.downloadButton);
    }
}
