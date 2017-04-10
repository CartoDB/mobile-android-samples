package com.carto.cartomap.sections.torqueapi.histogram.subviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;

import com.carto.cartomap.sections.torqueapi.histogram.TorqueHistogram;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by aareundo on 02/03/17.
 */

public class TorqueCounter extends TextView {

    public TorqueCounter(Context context) {
        super(context);

        setGravity(Gravity.CENTER);
        setTypeface(Typeface.create("sans-serif-thin", Typeface.BOLD));
        setTextSize(14f);
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

    static final int incrementBy = 15;
    public static ArrayList<String> timestamps;

    public void Update(int frameNumber, int frameCount) {

        if (timestamps == null) {

            timestamps = new ArrayList<>();

            /*
			 * Hardcoded (pretty) timestamp in accordance with the web UI.
			 * Non-hardcoded currently only available via external api
			 */

            Calendar date = new GregorianCalendar(2016, 9, 15, 12, 14, 0);

            for (int i = 0; i < 256; i++)
            {
                String time = " ";

                time += date.get(GregorianCalendar.HOUR_OF_DAY) + ":";
                time += date.get(GregorianCalendar.MINUTE) + " ";

                time += date.get(GregorianCalendar.DAY_OF_MONTH) + "/";
                time += date.get(GregorianCalendar.MONTH) + "/";
                time += date.get(GregorianCalendar.YEAR) + " ";

                timestamps.add(time);
                date.add((GregorianCalendar.MINUTE), incrementBy);
            }
        }

        setText(timestamps.get(frameNumber));
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
