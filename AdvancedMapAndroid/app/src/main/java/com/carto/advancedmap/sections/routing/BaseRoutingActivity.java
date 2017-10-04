package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.baseclasses.views.PackageManagerBaseView;
import com.carto.advancedmap.sections.basemap.BaseMapsView;
import com.carto.advancedmap.baseclasses.activities.PackageManagerBaseActivity;
import com.carto.advancedmap.utils.RouteCalculator;
import com.carto.advancedmap.utils.Sources;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.routing.RoutingService;

/**
 * Created by aareundo on 16/12/16.
 */

public class BaseRoutingActivity extends PackageManagerBaseActivity {

    RouteCalculator calculator;

    protected RoutingService service;

    @Override
    public String getFolderName() {
        return "com.carto.routingpackages";
    }

    @Override
    public String getSource() {
        return Sources.ROUTING_TAG + Sources.OFFLINE_ROUTING;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        contentView = new PackageManagerBaseView(this);
        setContentView(contentView);

        super.onCreate(savedInstanceState);

        contentView.addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        contentView.banner.alert("Long click on the map to set route start position");
    }

    protected void setService(RoutingService service) {

        this.service = service;

        /* Use getter for mapView as it can be (and is) overridden in subclass */
        calculator = new RouteCalculator(this, contentView.mapView, service);
    }
}
