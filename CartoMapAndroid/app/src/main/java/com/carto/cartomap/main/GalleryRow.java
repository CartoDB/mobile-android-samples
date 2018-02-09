package com.carto.cartomap.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carto.cartomap.util.Colors;

/**
 * Created by aareundo on 28/09/2017.
 */

public class GalleryRow extends RelativeLayout {

    ImageView image;
    TextView title, description;

    Sample sample;

    public GalleryRow(Context context, Sample sample) {
        super(context);

        int color = Color.WHITE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(new ColorDrawable(color));
        } else {
            setBackgroundColor(color);
        }

        image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(image);

        title = new TextView(context);
        title.setTextColor(Colors.APPLE_BLUE);
        title.setTextSize(14.0f);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        addView(title);

        description = new TextView(context);
        description.setTextColor(Colors.DARK_GRAY);
        description.setTextSize(12.0f);
        addView(description);

        update(sample);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(5);
        }
    }

    public void update(Sample sample) {
        this.sample = sample;

        title.setText(sample.title);
        title.measure(0, 0);

        description.setText(sample.description);

        image.setImageResource(sample.imageResource);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);

        int padding = (int)(5 * getResources().getDisplayMetrics().density);
        int imageHeight = params.height / 5 * 3;

        int x = padding;
        int y = padding;
        int w = params.width - 2 * padding;
        int h = imageHeight;

        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(w, h);
        parameters.leftMargin = x;
        parameters.topMargin = y;

        image.setLayoutParams(parameters);

        y += h + padding;
        h = title.getMeasuredHeight();

        parameters = new RelativeLayout.LayoutParams(w, h);
        parameters.leftMargin = x;
        parameters.topMargin = y;

        title.setLayoutParams(parameters);

        y += h + padding;
        h = params.height - (imageHeight + h + 2 * padding);

        parameters = new RelativeLayout.LayoutParams(w, h);
        parameters.leftMargin = x;
        parameters.topMargin = y;

        description.setLayoutParams(parameters);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            setAlpha(0.5f);
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            setAlpha(1.0f);
        }

        return super.onTouchEvent(event);
    }
}
