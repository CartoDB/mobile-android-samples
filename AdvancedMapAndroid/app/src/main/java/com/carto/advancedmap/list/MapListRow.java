package com.carto.advancedmap.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by aareundo on 14/10/16.
 */

public class MapListRow extends LinearLayout {

    TextView topBorder;
    TextView title;
    TextView description;

    public MapListRow(Context context, MapListItem item) {
        super(context);

        setOrientation(VERTICAL);

        topBorder = new TextView(context);
        addView(topBorder);

        title = new TextView(context);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);
        addView(title);

        description = new TextView(context);
        description.setTextColor(Color.DKGRAY);
        addView(description);
    }

    public void update(MapListItem item, int position) {

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);

        title.setText(item.name);
        title.setLayoutParams(params);

        ColorDrawable background;

        if (item.isHeader) {
            background = new ColorDrawable(Color.BLACK);

            if (position == 0) {
                topBorder.setLayoutParams(new LayoutParams(0, 0));
            } else {
                params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 15);
                topBorder.setLayoutParams(params);
            }

            description.setText("");
            description.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

            setBackgroundColor(Color.argb(255, 200, 200, 200));
        } else {
            background = null;
            topBorder.setLayoutParams(new LayoutParams(0, 0));
            description.setText(item.description);
            description.setLayoutParams(params);
            setBackgroundColor(Color.argb(255, 240, 240, 240));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            topBorder.setBackground(background);
        }
    }
}
