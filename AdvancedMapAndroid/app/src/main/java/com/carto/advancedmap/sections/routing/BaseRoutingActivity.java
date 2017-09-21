package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.sections.offlinemap.advancedpackagemanager.PackageManagerBaseActivity;
import com.carto.advancedmap.utils.RouteCalculator;
import com.carto.routing.RoutingService;
import com.carto.ui.MapView;

/**
 * Created by aareundo on 16/12/16.
 */

public class BaseRoutingActivity extends PackageManagerBaseActivity {

    RouteCalculator calculator;

    protected RoutingService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView.addBaseLayer(BaseMapsView.DEFAULT_STYLE);
    }

    protected void setService(RoutingService service) {

        this.service = service;

        /* Use getter for mapView as it can be (and is) overridden in subclass */
        calculator = new RouteCalculator(this, contentView.mapView, service);
    }
}
