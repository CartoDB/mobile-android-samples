package com.carto.cartomap.sections.torqueapi;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import com.carto.cartomap.sections.torqueapi.histogram.TorqueHistogram;
import com.carto.ui.MapView;

/**
 * Created by aareundo on 03/03/17.
 */

public class TorqueView extends RelativeLayout {

    public MapView MapView;

    public TorqueHistogram Histogram;

    int getHistogramMargin() {
        return (int) (5f * getContext().getResources().getDisplayMetrics().density);
    }

    public TorqueView(Context context) {

        super(context);

        MapView = new MapView(context);
        addView(MapView);

        MapView.setLayoutParams(new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));

        Histogram = new TorqueHistogram(context, (TorqueShipActivity)context);
        addView(Histogram);
    }

    void Dispose() {
        System.gc();
    }

    public void InitializeHistogram(int frameCount) {
        Histogram.Initialize(frameCount, getHistogramMargin());
    }
}
