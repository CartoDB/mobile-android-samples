package com.carto.cartomap.sections.torqueapi.histogram.subviews;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.carto.cartomap.R;
import com.carto.cartomap.sections.torqueapi.TorqueShipActivity;
import com.carto.cartomap.sections.torqueapi.histogram.TorqueHistogram;
import com.carto.cartomap.sections.torqueapi.histogram.TorqueHistogramInterface;

/**
 * Created by aareundo on 02/03/17.
 */

public class TorqueButton extends RelativeLayout {

    static int RESOURCE_PLAY = R.drawable.button_play;
    static int RESOURCE_PAUSE = R.drawable.button_pause;

    ImageView imageView;

    int imageResource;

    TorqueHistogramInterface histogramInterface;

    public TorqueButton(Context context, TorqueShipActivity activity) {

        super(context);

        imageView = new ImageView(context);
        addView(imageView);

        LayoutParams parameters = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );

        parameters.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(parameters);

        setImageResource(RESOURCE_PAUSE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(10);
        }

        setPadding(10, 10, 10, 10);

        histogramInterface = activity;
    }

    public boolean isPaused() {
        return  imageResource == RESOURCE_PLAY;
    }

    public void play() {
        setImageResource(RESOURCE_PAUSE);
    }
    public void pause() {
        setImageResource(RESOURCE_PLAY);
    }

    public void toggle() {

        if (isPaused()) {
            play();
        } else {
            pause();
        }
    }


    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
        imageView.setImageResource(imageResource);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(params.width / 2);
        drawable.setColor(TorqueHistogram.ButtonColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundColor(TorqueHistogram.ButtonColor);
        }

        super.setLayoutParams(params);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            animateToScale(1.02f);
        } else if (action == MotionEvent.ACTION_UP) {
            animateToScale(1.0f);
            toggle();
            histogramInterface.onButtonClicked();
        } else if (action == MotionEvent.ACTION_CANCEL) {
            animateToScale(1.0f);
        }

        return true;
    }

    void animateToScale(float scale) {
        animate().scaleX(scale).scaleY(scale).setDuration(100);
    }
}
