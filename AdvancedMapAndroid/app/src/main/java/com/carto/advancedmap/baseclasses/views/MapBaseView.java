package com.carto.advancedmap.baseclasses.views;

import android.content.Context;

import com.carto.advanced.kotlin.sections.base.utils.ExtensionsKt;
import com.carto.advanced.kotlin.sections.base.views.BaseView;
import com.carto.advancedmap.R;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

/**
 * Created by aareundo on 20/09/2017.
 */

public class MapBaseView extends BaseView {

    public MapView mapView;
    public Projection projection;

    public MapBaseView(Context context) {
        super(context);

        mapView = new MapView(context);
        addView(mapView);

        // Set projection
        projection = mapView.getOptions().getBaseProjection();

        setMainViewFrame();
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();

        ExtensionsKt.setFrame(mapView, 0, 0, getFrame().getWidth(), getFrame().getHeight());
    }

    public void addBaseLayer(CartoBaseMapStyle style)
    {
        CartoOnlineVectorTileLayer base = new CartoOnlineVectorTileLayer(style);
        mapView.getLayers().add(base);
    }
}
