package com.carto.advancedmap.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.carto.advanced.kotlin.sections.base.views.BaseView;
import com.carto.advanced.kotlin.utils.Colors;
import com.carto.advancedmap.model.Sample;

/**
 * Created by aareundo on 21/09/2017.
 */

public class GalleryRow extends BaseView {

    ImageView image;
    TextView title, description;

    Sample sample;

    public GalleryRow(Context context, Sample sample) {
        super(context);

        int color = Color.WHITE;

        if (isJellybeanOrHigher()) {
            setBackground(new ColorDrawable(color));
        } else {
            setBackgroundColor(color);
        }

        image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(image);

        title = new TextView(context);
        title.setTextColor(Colors.appleBlueInt);
        title.setTextSize(14.0f);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        addView(title);

        description = new TextView(context);
        description.setTextColor(Colors.darkGrayInt);
        description.setTextSize(12.0f);
        addView(description);

        update(sample);
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
    }

    public void update(Sample sample) {

    }
}
