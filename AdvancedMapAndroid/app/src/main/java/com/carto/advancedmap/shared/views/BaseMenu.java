package com.carto.advancedmap.shared.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.LinearLayout;

/**
 * Created by aareundo on 16/12/16.
 */

public class BaseMenu extends LinearLayout {

    protected Context context;

    MenuButton button;

    public BaseMenu(Context context) {
        super(context);

        this.context = context;

        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setBackground(new ColorDrawable(Color.argb(130, 0, 0, 0)));
        setVisibility(GONE);
    }

    public void setButton(MenuButton button) {
        this.button = button;
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

}
