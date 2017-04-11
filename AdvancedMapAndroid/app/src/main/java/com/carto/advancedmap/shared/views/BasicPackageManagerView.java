package com.carto.advancedmap.shared.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carto.core.MapPos;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOfflineVectorTileLayer;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.ui.MapView;

/**
 * Created by aareundo on 14/03/17.
 */

public class BasicPackageManagerView extends RelativeLayout {

    public MapView mapView;

    public TextView statusLabel;

    CartoPackageManager manager;

    public BasicPackageManagerView(Context context, CartoPackageManager manager)
    {
        super(context);

        this.manager = manager;

        mapView = new MapView(context);
        mapView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mapView);

        // Initialize & style Status label
        statusLabel = new TextView(context);
        statusLabel.setTextColor(Color.BLACK);

        GradientDrawable background = new GradientDrawable();
        background.setCornerRadius(5);
        background.setColor(Color.argb(160, 255, 255, 255));
        statusLabel.setBackground(background);

        statusLabel.setGravity(Gravity.CENTER);
        statusLabel.setTypeface(Typeface.create("HelveticaNeue", Typeface.NORMAL));

        DisplayMetrics screen = getResources().getDisplayMetrics();

        int width = screen.widthPixels / 2;
        int height = width / 4;

        int x = screen.widthPixels / 2 - width / 2;
        int y = screen.heightPixels / 100;

        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(width, height);
        parameters.setMargins(x, y, 0, 0);

        addView(statusLabel, parameters);
    }

    public void zoomTo(MapPos position)
    {
        mapView.setFocusPos(mapView.getOptions().getBaseProjection().fromWgs84(position), 0);
        mapView.setZoom(12, 2);
    }

    public void setBaseLayer()
    {
        CartoOfflineVectorTileLayer layer = new CartoOfflineVectorTileLayer(manager, CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(layer);
    }

    public void setOnlineBaseLayer()
    {
        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(layer);
    }
}
