package com.carto.advancedmap.baseclasses.views;

import android.content.Context;

import com.carto.advanced.kotlin.components.Banner;
import com.carto.advanced.kotlin.components.PopupButton;
import com.carto.advanced.kotlin.components.popup.SlideInPopup;
import com.carto.advanced.kotlin.sections.base.utils.ExtensionsKt;
import com.carto.advanced.kotlin.sections.base.views.BaseView;
import com.carto.advancedmap.R;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

import java.util.ArrayList;

/**
 * Created by aareundo on 20/09/2017.
 */

public class MapBaseView extends BaseView {

    public Banner banner;

    public MapView mapView;

    public SlideInPopup popup;

    public Projection projection;

    ArrayList<PopupButton> buttons = new ArrayList<>();

    public MapBaseView(Context context) {
        super(context);

        mapView = new MapView(context);
        addView(mapView);

        // Set projection
        projection = mapView.getOptions().getBaseProjection();

        banner = new Banner(context);
        banner.hide();
        addView(banner);

        popup = new SlideInPopup(context);
        addView(popup);

        setMainViewFrame();
    }

    protected int bottomLabelHeight, smallPadding, buttonSize;

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();

        ExtensionsKt.setFrame(mapView, 0, 0, getFrame().getWidth(), getFrame().getHeight());
        popup.setFrame(0, 0, getFrame().getWidth(), getFrame().getHeight());

        bottomLabelHeight = (int)(40 * getDensity());
        smallPadding = (int)(5 * getDensity());
        buttonSize = (int)(60 * getDensity());

        int count = buttons.size();
        int buttonWidth = buttonSize;
        int innerPadding = (int)(25 * getDensity());

        int totalArea = buttonWidth * count + (innerPadding * (count - 1));

        int w = buttonWidth;
        int h = w;
        int x = getFrame().getWidth() / 2 - totalArea / 2;
        int y = getFrame().getHeight() - (h + smallPadding + bottomLabelHeight);

        for (PopupButton button : buttons) {
            button.setFrame(x, y, w, h);
        }

        x = 0;
        y = 0;
        w = getFrame().getWidth();
        h = (int)(50 * getDensity());

        banner.setFrame(x, y, w, h);
    }

    public void addBaseLayer(CartoBaseMapStyle style)
    {
        CartoOnlineVectorTileLayer base = new CartoOnlineVectorTileLayer(style);
        mapView.getLayers().add(base);
    }

    public void addButton(PopupButton button) {
        buttons.add(button);
        addView(button);
    }
}
