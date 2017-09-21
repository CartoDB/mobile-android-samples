package com.carto.advancedmap.main;

import android.content.Context;

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
    }

    GalleryRow[] views = {};

    public void addRows(Class[] samples) {

        for (Class sample : samples) {

        }
    }
}
