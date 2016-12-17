package com.carto.advancedmap.shared.views;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.carto.advancedmap.R;

/**
 * Created by aareundo on 16/12/16.
 */


public class MenuButton extends RelativeLayout
{
    Context context;

    BaseMenu menu;

    public MenuButton(Context context, BaseMenu menu)
    {
        super(context);
        this.context = context;
        this.menu = menu;

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
