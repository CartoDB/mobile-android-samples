package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.baseclasses.views.PackageManagerBaseView;
import com.carto.advancedmap.sections.basemap.BaseMapsView;
import com.carto.advancedmap.baseclasses.activities.PackageManagerBaseActivity;
import com.carto.advancedmap.utils.RouteCalculator;
import com.carto.advancedmap.utils.Sources;
import com.carto.core.MapPos;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.VectorElementEventListener;
import com.carto.routing.RoutingService;
import com.carto.styles.BalloonPopupStyle;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.VectorElementClickInfo;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.VectorElement;

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

    BalloonPopup previous;

    @Override
    protected void onResume() {
        super.onResume();

        contentView.banner.alert("Long click on the map to set route start position");

        calculator.getRouteLayer().setVectorElementEventListener(new VectorElementEventListener() {
            @Override
            public boolean onVectorElementClicked(VectorElementClickInfo clickInfo) {

                if (previous != null) {
                    calculator.getRouteSource().remove(previous);
                }

                VectorElement element = clickInfo.getVectorElement();

                String title = "DATA";
                String description = element.getMetaDataElement(RouteCalculator.DESCRIPTION).getString();
                description = description.replace("RoutingInstruction ", "");

                BalloonPopupStyleBuilder builder = new BalloonPopupStyleBuilder();
                // Set a higher placement priority so it would always be visible
                builder.setPlacementPriority(10);

                MapPos position = clickInfo.getClickPos();
                BalloonPopupStyle style = builder.buildStyle();

                BalloonPopup popup = new BalloonPopup(position, style, title, description);

                calculator.getRouteSource().add(popup);
                previous = popup;
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        calculator.getRouteLayer().setVectorElementEventListener(null);
    }

    protected void setService(RoutingService service) {

        this.service = service;

        /* Use getter for mapView as it can be (and is) overridden in subclass */
        calculator = new RouteCalculator(this, contentView.mapView, service);
    }
}
