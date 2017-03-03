package com.carto.cartomap.sections.torqueapi.histogram.subviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;

import com.carto.cartomap.sections.torqueapi.histogram.TorqueHistogram;

/**
 * Created by aareundo on 02/03/17.
 */

public class TorqueCounter extends TextView {

    public TorqueCounter(Context context) {
        super(context);

        setGravity(Gravity.CENTER);
        setTypeface(Typeface.create("sans-serif-thin", Typeface.BOLD));
        setTextSize(17f);
        setTextColor(Color.WHITE);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(5);
        drawable.setColor(TorqueHistogram.BackgroundColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundColor(TorqueHistogram.BackgroundColor);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(10);
        }
    }

    public void Update(int frameNumber, int frameCount) {

        String number = "";

        if (frameCount > 100) {
            if (frameNumber < 10) {
                number = "00" + frameNumber;
            } else if (frameNumber < 100) {
                number = "0" + frameNumber;
            } else {
                number = Integer.toString(frameNumber);
            }
        }

        setText("  " + number + "/" + frameCount + "  ");
    }

    public void Update(int frameNumber) {

        String text = ((String)getText());

        if (!text.contains("/")) {
            return;
        }

        int frameCount = 0;


        boolean success = false;

        try {
            frameCount = Integer.parseInt(text.split("/")[1].trim());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!success) {
            return;
        }

        Update(frameNumber, frameCount);
    }
}
