package com.carto.advancedmap.sections.routing.offline;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.carto.advancedmap.sections.routing.OfflineRoutingActivity_New;
import com.carto.advancedmap.shared.packages.PackageAdapter;
import com.carto.advancedmap.shared.views.BaseMenu;
import com.carto.advancedmap.shared.views.MenuButton;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.ui.MapView;

/**
 * Created by aareundo on 16/12/16.
 */

public class OfflineRoutingView extends RelativeLayout {

    public MapView mapView;

    public MenuButton button;

    public PackageManagerMenu menu;

    public OfflineRoutingView(Context context, PackageAdapter adapter) {
        super(context);

        mapView = new MapView(context);

        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(layer);

        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mapView);

        menu = new PackageManagerMenu(context, adapter);
        addView(menu);

        button = new MenuButton(context, menu);
        menu.setButton(button);

        addView(button);
    }

    public class PackageManagerMenu extends BaseMenu {

        ListView list;

        public PackageManagerMenu(Context context, PackageAdapter adapter) {

            super(context);

            list = new ListView(context);
            list.setAdapter(adapter);
            list.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            addView(list);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            int action = event.getAction();
            int x = (int)event.getX();
            int y = (int)event.getY();

            if (action == MotionEvent.ACTION_UP) {

                boolean isInBox = false;

                    Rect rect = new Rect();

                    list.getHitRect(rect);

                    if (rect.contains(x, y)) {
                        isInBox = true;
                    }

                if (!isInBox) {
                    // hide if background was clicked
                    hide();
                }
            }

            return true;
        }
    }
}
