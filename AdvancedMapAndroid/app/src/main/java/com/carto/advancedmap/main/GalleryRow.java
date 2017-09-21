package com.carto.advancedmap.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.carto.advanced.kotlin.sections.base.utils.ExtensionsKt;
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

        int padding = 5;
        int imageHeight = getFrame().getHeight() / 5 * 3;

        int x = padding;
        int y = padding;
        int w = getFrame().getWidth() - 2 * padding;
        int h = imageHeight;

        ExtensionsKt.setFrame(image, x, y, w, h);

        y += h + padding;
        h = title.getMeasuredHeight();

        ExtensionsKt.setFrame(title, x, y, w, h);

        y += h + padding;
        h = getFrame().getHeight() - (imageHeight + h + 2 * padding);

        ExtensionsKt.setFrame(description, x, y, w, h);
    }

    public void update(Sample sample) {
        this.sample = sample;

        title.setText(sample.title);
        title.measure(0, 0);

        description.setText(sample.description);

        image.setImageResource(sample.imageResource);

        layoutSubviews();
    }
}
