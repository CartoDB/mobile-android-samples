package com.carto.cartomap.sections.torqueapi.histogram.subviews;

import android.content.Context;
import android.widget.RelativeLayout;

import com.carto.cartomap.sections.torqueapi.histogram.TorqueHistogram;

/**
 * Created by aareundo on 02/03/17.
 */

public class TorqueIndicator extends RelativeLayout {

    public TorqueIndicator(Context context) {
        super(context);
        setBackgroundColor(TorqueHistogram.IndicatorColor);
    }

    public void Update(int frameNumber) {
        int width = getLayoutParams().width;
        ((LayoutParams) getLayoutParams()).setMargins(
                width * frameNumber,
                ((LayoutParams) getLayoutParams()).topMargin,
                ((LayoutParams) getLayoutParams()).rightMargin,
                ((LayoutParams) getLayoutParams()).bottomMargin
        );
    }
}
