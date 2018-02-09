package com.carto.advancedmap.main;

import android.content.Context;

import com.carto.advanced.kotlin.sections.base.utils.ExtensionsKt;
import com.carto.advanced.kotlin.sections.base.views.BaseScrollView;
import com.carto.advanced.kotlin.sections.base.views.BaseView;

import java.util.ArrayList;

/**
 * Created by aareundo on 21/09/2017.
 */

public class MainView extends BaseView {

    BaseScrollView container;

    public MainView(Context context) {
        super(context);

        container = new BaseScrollView(context);
        container.matchParent();
        addView(container);

        setMainViewFrame();
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();

        int itemsInRow = 2;

        if (ExtensionsKt.isLandScape(this)) {
            itemsInRow = 3;

            if (ExtensionsKt.isLargeTablet(this)) {
                itemsInRow = 4;
            }
        } else if (ExtensionsKt.isLargeTablet(this)) {
            itemsInRow = 3;
        }

        int padding = (int)(getDensity() * 5);

        int x = padding;
        int y = padding;
        int w = (getFrame().getWidth() - (itemsInRow + 1) * padding) / itemsInRow;
        int h = w;

        for (GalleryRow view : views) {

            view.setFrame(x, y, w, h);

            if (x == ((w * (itemsInRow - 1)) + padding * itemsInRow)) {
                y += h + padding;
                x = padding;
            } else {
                x += w + padding;
            }
        }
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

        layoutSubviews();
    }
}
