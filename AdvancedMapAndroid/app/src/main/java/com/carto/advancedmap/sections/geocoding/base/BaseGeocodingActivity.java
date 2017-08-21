package com.carto.advancedmap.sections.geocoding.base;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.carto.advancedmap.sections.geocoding.adapter.GeocodingResultAdapter;
import com.carto.advancedmap.shared.Colors;
import com.carto.geocoding.GeocodingService;

/**
 * Base class for non-reverse (type address) geocoding
 */
public class BaseGeocodingActivity extends GeocodingBaseActivity {

    EditText inputField;
    ListView resultTable;
    GeocodingResultAdapter adapter;

    protected GeocodingService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputField = new EditText(this);
        inputField.setTextColor(Color.WHITE);
        inputField.setBackgroundColor(Colors.DARK_TRANSPARENT_GRAY);
        inputField.setHint("Type address...");
        inputField.setHintTextColor(Color.LTGRAY);
        inputField.setSingleLine();

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int padding = (int)(5 * getResources().getDisplayMetrics().density);

        int width = totalWidth - 2 * padding;
        int height = (int)(45 * getResources().getDisplayMetrics().density);

        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(width, height);
        parameters.setMargins(padding, padding, 0, 0);
        addContentView(inputField, parameters);

        resultTable = new ListView(this);
        resultTable.setBackgroundColor(Colors.LIGHT_TRANSPARENT_GRAY);

        height = (int)(200 * getResources().getDisplayMetrics().density);

        parameters = new RelativeLayout.LayoutParams(width, height);
        parameters.setMargins(padding, padding, 0, 0);

        addContentView(resultTable, parameters);

        adapter = new GeocodingResultAdapter(this);
        adapter.width = width;
        resultTable.setAdapter(adapter);
    }
}
