package com.carto.cartomap.android.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by aareundo on 13/10/16.
 */


public class MapListRow extends LinearLayout {

    TextView topBorder;
    TextView lineOne;
    TextView lineTwo;

    public MapListRow(Context context, MapListItem item) {
        super(context);

        setOrientation(VERTICAL);

        topBorder = new TextView(context);
        addView(topBorder);

        lineOne = new TextView(context);
        lineOne.setTextColor(Color.WHITE);
        lineOne.setTypeface(null, Typeface.BOLD);
        addView(lineOne);

        lineTwo = new TextView(context);
        lineTwo.setTextColor(Color.WHITE);
        addView(lineTwo);

        update(item);
    }

    public void update(MapListItem item) {

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);

        lineOne.setText(item.name);
        lineOne.setLayoutParams(params);

        ColorDrawable background;

        if (item.isHeader) {
            background = new ColorDrawable(Color.WHITE);
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
            params.setMargins(10, 10, 10, 10);
            topBorder.setLayoutParams(params);
            lineTwo.setText("");
        } else {
            background = null;
            topBorder.setLayoutParams(new LayoutParams(0, 0));
            lineTwo.setText(item.description);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            topBorder.setBackground(background);
            lineTwo.setBackground(background);
        }

        lineTwo.setLayoutParams(params);
    }
}
