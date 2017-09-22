package com.carto.advancedmap.sections.basemap.views.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carto.advancedmap.utils.Colors;
import com.carto.advancedmap.sections.basemap.model.NameValuePair;
import com.carto.advancedmap.sections.basemap.model.Section;
import com.carto.advancedmap.sections.basemap.model.SectionType;

import java.util.ArrayList;

/**
 * Created by aareundo on 08/11/16.
 */

public class OptionMenuItem extends LinearLayout
{
    LinearLayout headerContainer;
    RelativeLayout contentContainer;

    TextView osmLabel, tileTypeLabel;

    ArrayList<OptionLabel> optionLabels;

    private int headerHeight;

    Context context;

    public OptionMenuItem(Context context) {
        super(context);
        this.context = context;

        int padding = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.05);

        optionLabels = new ArrayList<>();

        setOrientation(VERTICAL);

        int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.9);

        headerContainer = new LinearLayout(context);
        headerContainer.setBackgroundColor(Colors.ACTION_BAR);
        headerContainer.setOrientation(HORIZONTAL);

        addView(headerContainer);

        contentContainer = new RelativeLayout(context);
        contentContainer.setLayoutParams(new RelativeLayout.LayoutParams(width, 100));
        addView(contentContainer);

        osmLabel = getHeaderItem(padding, Typeface.BOLD);
        osmLabel.setGravity(Gravity.CENTER_VERTICAL);
        tileTypeLabel = getHeaderItem(padding, Typeface.NORMAL);
        tileTypeLabel.setGravity(Gravity.CENTER);

        headerContainer.addView(osmLabel);
        headerContainer.addView(tileTypeLabel);

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT, 0.8f);
        parameters.setMargins(padding, padding, padding,  0);
        setLayoutParams(parameters);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setAlpha(1);
        } else {
            setAlpha(0.6f);
        }

        super.setEnabled(enabled);
    }

    TextView getHeaderItem(int padding, int style)
    {
        TextView view = new TextView(context);

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.5f);
        parameters.setMargins(padding, 0, 0, 0);
        view.setLayoutParams(parameters);

        view.setTypeface(Typeface.create("Helvetica Neue", style));
        view.setTextColor(Color.WHITE);
        view.setPadding(0, 20, 0, 20);

        return view;
    }

    Section section;

    public Section getSection() {
        return section;
    }

    public void setSection(Section section)
    {
        this.section = section;

        osmLabel.setText(section.getOSM().getName().toUpperCase());

        if (section.getType() != SectionType.LANGUAGE) {
            tileTypeLabel.setText(section.getType().toString().toUpperCase());
        }

        for (NameValuePair option : section.getStyles()) {
            OptionLabel label = new OptionLabel(context, option);
            optionLabels.add(label);
            contentContainer.addView(label);
        }

        contentContainer.measure(0, 0);

        int rowHeight = context.getResources().getDisplayMetrics().heightPixels / 16;

        if (optionLabels.size() <= 3) {
            contentContainer.getLayoutParams().height = rowHeight;
        } else if (optionLabels.size() <= 6) {
            contentContainer.getLayoutParams().height = 2 * rowHeight;
        } else if (optionLabels.size() <= 9) {
            contentContainer.getLayoutParams().height = 3 * rowHeight;
        } else {
            // Not supported
        }

        int counter  = 1;
        int total = optionLabels.size();
        int width = contentContainer.getMeasuredWidth();

        int y = 0;
        int h = rowHeight;

        for (OptionLabel label : optionLabels) {

            int x;
            int w;

            if (total == 2) {
                w = width / 2;
            } else if (total >= 3) {
                w = width / 3;
            } else {
                w = width;
            }

            if (counter % 3 == 0) {
                x = width / 3 * 2;
            } else if (counter % 2 == 0) {
                if (total == 2) {
                    x = width / 2;
                } else {
                    x = width / 3;
                }
            } else {
                x = 0;
            }

            label.setLayout(x, y, w, h);

            if (counter % 3 == 0) {
                y += rowHeight;
            }
            counter++;
        }
     }

    public Rect getHitRect() {

        Rect result = new Rect();
        getHitRect(result);

        return result;
    }

    public int getHeaderHeight() {

        if (headerHeight == 0) {
            headerContainer.measure(0, 0);
            headerHeight = headerContainer.getMeasuredHeight();
        }

        return headerHeight;
    }

    public ArrayList<OptionLabel> getOptions() {
        return optionLabels;
    }

    public OptionLabel setFirstItemActive() {

        for (OptionLabel label : optionLabels) {
            label.normalize();
        }

        if (optionLabels.size() > 0)
        {
            OptionLabel label = optionLabels.get(0);
            label.highlight();
            return label;
        }

        return null;
    }
}
