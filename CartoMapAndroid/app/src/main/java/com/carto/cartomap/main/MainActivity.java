package com.carto.cartomap.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.carto.cartomap.util.Colors;

public class MainActivity extends AppCompatActivity {

    public static String TITLE = "activity_title";
    public static String DESCRIPTION = "activity_description";

    MainView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView = new MainView(this);
        setContentView(contentView);

        contentView.addRows(Samples.LIST);

        ColorDrawable drawable = new ColorDrawable(Colors.LOCATION_RED);
        getSupportActionBar().setBackgroundDrawable(drawable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (final GalleryRow row : contentView.views) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity context = MainActivity.this;

                    Intent intent = new Intent(context, row.sample.activity);
                    intent.putExtra(TITLE, row.sample.title);
                    intent.putExtra(DESCRIPTION, row.sample.description);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (GalleryRow row : contentView.views) {
            row.setOnClickListener(null);
        }
    }
}
