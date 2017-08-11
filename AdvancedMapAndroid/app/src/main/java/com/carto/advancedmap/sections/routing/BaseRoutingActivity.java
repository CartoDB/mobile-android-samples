package com.carto.advancedmap.sections.routing;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.carto.advancedmap.R;
import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.advancedmap.shared.activities.MapBaseActivity;
import com.carto.advancedmap.utils.RouteCalculator;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Bitmap;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.VectorLayer;
import com.carto.routing.RoutingAction;
import com.carto.routing.RoutingInstruction;
import com.carto.routing.RoutingInstructionVector;
import com.carto.routing.RoutingRequest;
import com.carto.routing.RoutingResult;
import com.carto.routing.RoutingService;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.ui.ClickType;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.MapView;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Marker;

import java.io.IOException;

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
