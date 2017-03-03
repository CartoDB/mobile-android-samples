package com.carto.cartomap.sections.torqueapi.histogram.subviews;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by aareundo on 02/03/17.
 */

public class TorqueInterval extends RelativeLayout {

    int ElementCount;

    public TorqueInterval(Context context, int width, int margin, int height) {

        super(context);

        setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(width, height);
        parameters.setMargins(0, 0, margin, 0);

        setLayoutParams(parameters);
    }

    public void Update(int parentHeight, int elementCount, int maxElements) {

        if (elementCount == 0) {
            getLayoutParams().height = 0;

            return;
        }

        if (ElementCount == elementCount) {
            return;
        }

        ElementCount = elementCount;

        int percent = (elementCount * 100) / maxElements;
        int height = (parentHeight * percent) / 100;
        int margin = parentHeight - height;

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(getLayoutParams().width, height);
        parameters.setMargins(0, margin, parameters.rightMargin, 0);

        setLayoutParams(parameters);
    }

    public void Update(int parentHeight, int maxElements) {

        int percent = (ElementCount * 100) / maxElements;
        int height = (parentHeight * percent) / 100;
        int margin = parentHeight - height;

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(getLayoutParams().width, height);
        parameters.setMargins(0, margin, parameters.rightMargin, 0);

        setLayoutParams(parameters);
    }

}
