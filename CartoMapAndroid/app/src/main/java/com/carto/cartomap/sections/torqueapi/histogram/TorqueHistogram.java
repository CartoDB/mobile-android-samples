package com.carto.cartomap.sections.torqueapi.histogram;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carto.cartomap.sections.torqueapi.TorqueShipActivity;
import com.carto.cartomap.sections.torqueapi.histogram.subviews.TorqueButton;
import com.carto.cartomap.sections.torqueapi.histogram.subviews.TorqueCounter;
import com.carto.cartomap.sections.torqueapi.histogram.subviews.TorqueIndicator;
import com.carto.cartomap.sections.torqueapi.histogram.subviews.TorqueInterval;

import java.util.ArrayList;

/**
 * Created by aareundo on 02/03/17.
 */

public class TorqueHistogram extends RelativeLayout {

    public static int BackgroundColor = Color.argb(200, 215, 82, 75);
    public static int ButtonColor = Color.rgb(215, 82, 75);
    public static int IndicatorColor = Color.rgb(14, 122, 254);

    LinearLayout container;
    RelativeLayout outerContainer;

    TorqueIndicator indicator;

    ArrayList<TorqueInterval> intervals;

    int getBarHeight() {
        return (int)(getContext().getResources().getDisplayMetrics().density * 60);
    }

    int getCounterHeight() {
        return (int)(getContext().getResources().getDisplayMetrics().density * 22);
    }

    int getTotalHeight() {
        return getBarHeight() + getCounterHeight();
    }

    public TorqueCounter Counter;

    public TorqueButton Button;

    TorqueHistogramInterface histogramInterface;

    public TorqueHistogram(Context context, TorqueShipActivity activity) {

        super(context);

        intervals = new ArrayList<>();

        outerContainer = new RelativeLayout(context);
        addView(outerContainer);

        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(BackgroundColor);
        drawable.setCornerRadius(5);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            container.setBackground(drawable);
        } else {
            container.setBackgroundColor(BackgroundColor);
        }

        container.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );

        indicator = new TorqueIndicator(context);

        Counter = new TorqueCounter(context);

        Button = new TorqueButton(context, activity);

        histogramInterface = activity;
    }

    public void Initialize(int frameCount, int margin)
    {
        outerContainer.addView(container);

        addView(indicator);
        addView(Counter);
        addView(Button);

        int intervalMargin = 0;
        int intervalHeight = 0;
        int intervalWidth = getIntervalWidth();

        int width = frameCount * intervalWidth + frameCount * intervalMargin;
        RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(
                width, ViewGroup.LayoutParams.MATCH_PARENT
        );

        containerParams.setMargins(0, getCounterHeight() + margin, 0, margin);
        outerContainer.setLayoutParams(containerParams);

        for (int i = 0; i < frameCount; i++)
        {
            TorqueInterval bar = new TorqueInterval(getContext(), intervalWidth, intervalMargin, intervalHeight);
            container.addView(bar);
            intervals.add(bar);
        }

        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getTotalHeight()
        );

        parameters.addRule(ALIGN_PARENT_BOTTOM);
        parameters.setMargins(margin, 0, 0, 0);
        setLayoutParams(parameters);

        RelativeLayout.LayoutParams counterParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, getCounterHeight()
        );

        counterParams.addRule(ALIGN_PARENT_TOP);
        Counter.setLayoutParams(counterParams);

        RelativeLayout.LayoutParams indictorParams = new RelativeLayout.LayoutParams(
                getIntervalWidth(), ViewGroup.LayoutParams.MATCH_PARENT
        );
        indictorParams.topMargin = getCounterHeight();
        indicator.setLayoutParams(indictorParams);

        int buttonSize = getBarHeight() - 2 * margin;

        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(buttonSize, buttonSize);
        buttonParams.addRule(ALIGN_PARENT_RIGHT);
        buttonParams.addRule(ALIGN_PARENT_BOTTOM);
        buttonParams.setMargins(margin, margin, margin, margin);
        Button.setLayoutParams(buttonParams);
    }

    int getIntervalWidth() {
        return (int)(getContext().getResources().getDisplayMetrics().density * 2.0f);
    }

    public void UpdateElement(int frameNumber, int elementCount, int maxElements)
    {
        if (intervals.size() == 0)
        {
            return;
        }

        intervals.get(frameNumber).Update(getTotalHeight(), elementCount, maxElements);
        indicator.Update(frameNumber);
    }

    public void UpdateAll(int maxElements)
    {
        for (TorqueInterval interval : intervals)
        {
            interval.Update(getTotalHeight(), maxElements);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x = (int)event.getX();
            int number = x / getIntervalWidth();

            indicator.Update(number);

            histogramInterface.onHistogramClicked(number);
        }

        return false;
    }
}
