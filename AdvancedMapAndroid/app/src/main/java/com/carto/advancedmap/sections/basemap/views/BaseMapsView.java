package com.carto.advancedmap.sections.basemap.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carto.advancedmap.sections.basemap.BaseMapActivity;
import com.carto.advancedmap.sections.basemap.model.Section;
import com.carto.advancedmap.sections.basemap.model.SectionType;
import com.carto.advancedmap.sections.basemap.views.components.OptionLabel;
import com.carto.advancedmap.sections.basemap.views.components.OptionMenuItem;
import com.carto.advancedmap.shared.views.BaseMenu;
import com.carto.advancedmap.shared.views.MenuButton;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.ui.MapView;

import java.util.ArrayList;

/**
 * Created by aareundo on 08/11/16.
 */

public class BaseMapsView extends RelativeLayout
{
    public static final CartoBaseMapStyle DEFAULT_STYLE = CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER;

    public MapView mapView;

    public MenuButton button;

    public OptionMenu menu;

    public BaseMapsView(Context context)
    {
        super(context);

        mapView = new MapView(context);

        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(DEFAULT_STYLE);
        mapView.getLayers().add(layer);

        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mapView);

        menu = new OptionMenu(context);
        addView(menu);

        button = new MenuButton(context, menu);
        menu.setButton(button);

        addView(button);
    }

    public class OptionMenu extends BaseMenu
    {
        LinearLayout contentContainer;

        ArrayList<OptionMenuItem> views;
        ArrayList<Section> items;

        public OptionMenu(Context context)
        {
            super(context);

            views = new ArrayList<>();

            contentContainer = new LinearLayout(context);
            contentContainer.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            contentContainer.setOrientation(VERTICAL);

            addView(contentContainer);
        }

        public void setItems(ArrayList<Section> sections)
        {
            this.items = sections;

            for (Section section: sections) {
                OptionMenuItem view = new OptionMenuItem(context);
                view.setSection(section);
                views.add(view);
                contentContainer.addView(view);
            }
        }

        public void setLanguageMenuEnabled(boolean enabled) {

            OptionMenuItem language = getLanguageMenu();
            language.setEnabled(enabled);
            if (enabled) {

            } else {

            }
        }

        OptionMenuItem getLanguageMenu() {
            for (OptionMenuItem view : views) {
                if (view.getSection().getType() == SectionType.LANGUAGE) {
                    return view;
                }
            }

            return null;
        }

        public void setInitialItem(Section section)
        {
            for (OptionMenuItem view : views)
            {
                if (view.getSection().equals(section))
                {
                    if (section.getType() == SectionType.LANGUAGE)
                    {
                        currentLanguage = view.setFirstItemActive();
                    }
                    else
                    {
                        current = view.setFirstItemActive();
                    }
                }
            }
        }

        OptionLabel current;
        OptionLabel currentLanguage;

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            int action = event.getAction();
            int x = (int)event.getX();
            int y = (int)event.getY();

            if (action == MotionEvent.ACTION_UP) {

                boolean isInBox = false;

                for (OptionMenuItem item : views) {
                    Rect outerRect = item.getHitRect();

                    if (outerRect.contains(x, y)) {
                        isInBox = true;
                        int headerHeight = item.getHeaderHeight();

                        for (OptionLabel option : item.getOptions()) {

                            if (option.getGlobalRect(headerHeight, outerRect).contains(x, y)) {

                                if (!item.isEnabled()) {
                                    return true;
                                }

                                if (item.getSection().getType() == SectionType.LANGUAGE) {

                                    if (currentLanguage != null) {
                                        currentLanguage.normalize();
                                    }

                                    option.highlight();
                                    currentLanguage = option;
                                } else {

                                    if (current != null) {
                                        current.normalize();
                                    }

                                    option.highlight();
                                    current = option;
                                }

                                ((BaseMapActivity)context).updateBaseLayer(item.getSection(), option.getValue());
                            }
                        }
                    }
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
