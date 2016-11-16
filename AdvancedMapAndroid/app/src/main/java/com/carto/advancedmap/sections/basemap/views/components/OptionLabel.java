package com.carto.advancedmap.sections.basemap.views.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carto.advancedmap.Colors;
import com.carto.advancedmap.sections.basemap.model.NameValuePair;

/**
 * Created by aareundo on 08/11/16.
 */

public class OptionLabel extends TextView
{
    public String name;

    public String value;

    ColorDrawable background;

    public OptionLabel(Context context, NameValuePair option) {

        super(context);

        name = option.getName();
        value = option.getValue();

        setText(name.toUpperCase());
        setTypeface(Typeface.create("Helvetica-Neue", Typeface.NORMAL));
        setGravity(Gravity.CENTER);

        background = new ColorDrawable();
        setBackground(background);

        normalize();
    }

    public boolean isActive()
    {
        return background.getColor() != Color.WHITE;
    }

    public Rect getGlobalRect(int headerHeight, Rect outerRect) {

        Rect rect = getHitRect();

        int left = outerRect.left + rect.left;
        int top = outerRect.top + headerHeight + rect.top;
        int right = outerRect.left + rect.right;
        int bottom = outerRect.top + headerHeight + rect.bottom;

        return new Rect(left, top, right, bottom);
    }

    public Rect getHitRect() {

        Rect result = new Rect();
        getHitRect(result);

        return result;
    }

    public void highlight() {
        background.setColor(Colors.ACTIVE_MENU_ITEM);
        setTextColor(Color.WHITE);
    }

    public void normalize() {

        background.setColor(Color.WHITE);
        setTextColor(Color.argb(255, 50, 50, 50));
    }

    public String getValue() {
        return value;
    }

    public void setLayout(int x, int y, int w, int h) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.setMargins(x, y, 0, 0);
        setLayoutParams(params);
    }
}
