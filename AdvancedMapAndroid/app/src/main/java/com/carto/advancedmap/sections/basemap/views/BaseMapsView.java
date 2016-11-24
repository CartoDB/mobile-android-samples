package com.carto.advancedmap.sections.basemap.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carto.advancedmap.R;
import com.carto.advancedmap.sections.basemap.BaseMapActivity;
import com.carto.advancedmap.sections.basemap.model.Section;
import com.carto.advancedmap.sections.basemap.model.SectionType;
import com.carto.advancedmap.sections.basemap.views.components.OptionLabel;
import com.carto.advancedmap.sections.basemap.views.components.OptionMenuItem;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.ui.MapView;

import java.util.ArrayList;

/**
 * Created by aareundo on 08/11/16.
 */

public class BaseMapsView extends RelativeLayout
{
    public MapView mapView;

    public MenuButton button;

    public OptionMenu menu;

    public BaseMapsView(Context context)
    {
        super(context);

        mapView = new MapView(context);

        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(layer);

        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mapView);

        button = new MenuButton(context);
        addView(button);

        menu = new OptionMenu(context);
        addView(menu);
    }

    public class MenuButton extends RelativeLayout
    {
        Context context;

        MenuButton(Context context)
        {
            super(context);
            this.context = context;

            setBackgroundResource(R.drawable.icon_menu_round);

            int size = (int)(getSmallerScreenMetric() / 6.5);
            int margin = size / 5;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
            params.setMargins(0, 0, margin, margin);
            params.addRule(ALIGN_PARENT_RIGHT);
            params.addRule(ALIGN_PARENT_BOTTOM);

            setLayoutParams(params);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                setElevation(10);
            } else {
                // No elevation for you, my friend
            }
        }

        int getSmallerScreenMetric() {

            int width = context.getResources().getDisplayMetrics().widthPixels;
            int height = context.getResources().getDisplayMetrics().heightPixels;

            if (width > height) {
                return height;
            }

            return width;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                animateToScale(1.05f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                animateToScale(1.0f);

                if (menu.isVisible()) {
                    menu.hide();
                } else {
                    menu.show();
                }
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                animateToScale(1.0f);
            }

            return true;
        }

        void animateToScale(float scale) {
            animate().scaleY(scale).scaleX(scale).setDuration(100);
        }
    }

    public class OptionMenu extends LinearLayout
    {
        LinearLayout contentContainer;

        ArrayList<OptionMenuItem> views;
        ArrayList<Section> items;

        Context context;

        OptionMenu(Context context)
        {
            super(context);
            this.context = context;

            views = new ArrayList<>();

            setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            contentContainer = new LinearLayout(context);
            contentContainer.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            contentContainer.setOrientation(VERTICAL);

            addView(contentContainer);

            setBackground(new ColorDrawable(Color.argb(130, 0, 0, 0)));
            setVisibility(GONE);
        }

        public boolean isVisible()
        {
            return  getVisibility() == VISIBLE;
        }

        public void show()
        {
            setVisibility(VISIBLE);
            button.bringToFront();
        }

        public void hide()
        {
            setVisibility(INVISIBLE);
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
