package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.utils.RouteCalculator;
import com.carto.routing.RoutingService;
import com.carto.ui.MapView;

/**
 * Created by aareundo on 16/12/16.
 */

public class BaseRoutingActivity extends MapBaseActivity {

    RouteCalculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addBaseLayer();
    }

    protected MapView getMapView() {
        return mapView;
    }

    protected void addBaseLayer() {
        // Add online base layer, even used in Offline routing example
        addBaseLayer(BaseMapsView.DEFAULT_STYLE);
    }

    protected RoutingService service;

    protected void setService(RoutingService service) {

        this.service = service;

        /* Use getter for mapView as it can be (and is) overridden in subclass */
        calculator = new RouteCalculator(this, getMapView(), service);
    }
}
