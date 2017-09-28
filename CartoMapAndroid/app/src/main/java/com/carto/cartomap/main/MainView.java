package com.carto.cartomap.main;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.carto.cartomap.util.Colors;

import java.util.ArrayList;

/**
 * Created by aareundo on 28/09/2017.
 */

public class MainView extends ScrollView {

    RelativeLayout container;

    public MainView(Context context) {
        super(context);

        container = new RelativeLayout(context);
        addView(container);

        container.setBackgroundColor(Colors.NEAR_WHITE);
    }

    ArrayList<GalleryRow> views = new ArrayList<>();

    public void addRows(Sample[] samples) {

        for (GalleryRow view : views) {
            container.removeView(view);
        }

        views.clear();

        for (Sample sample : samples) {

            GalleryRow view = new GalleryRow(getContext(), sample);
            views.add(view);
            container.addView(view);
        }

        int itemsInRow = 2;

        if (isLandScape()) {
            itemsInRow = 3;

            if (isLargeTablet()) {
                itemsInRow = 4;
            }
        } else if (isLargeTablet()) {
            itemsInRow = 3;
        }

        int padding = (int)(getDensity() * 5);

        int x = padding;
        int y = padding;
        int w = (getMetrics().widthPixels - (itemsInRow + 1) * padding) / itemsInRow;
        int h = w;

        for (GalleryRow view : views) {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            params.leftMargin = x;
            params.topMargin = y;

            view.setLayoutParams(params);

            if (x == ((w * (itemsInRow - 1)) + padding * itemsInRow)) {
                y += h + padding;
                x = padding;
            } else {
                x += w + padding;
            }
        }
    }

    DisplayMetrics getMetrics() {
        return getContext().getResources().getDisplayMetrics();
    }

    float getDensity() {
        return getMetrics().density;
    }

    boolean isLandScape() {
        return getMetrics().widthPixels > getMetrics().heightPixels;
    }

    boolean isLargeTablet() {

        DisplayMetrics metrics = getMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float density = metrics.density;

        int greater = height;
        int lesser = width;

        if (isLandScape()) {
            greater = width;
            lesser = height;
        }

        if (density > 2.5) {
            // If density is too large, it'll be a phone
            return false;
        }

        return greater > 1920 && lesser > 1080;
    }
}
