package com.carto.advancedmap.sections.geocoding.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carto.advancedmap.sections.geocoding.GeocodingUtils;
import com.carto.core.Address;
import com.carto.geocoding.GeocodingResult;

/**
 * Created by aareundo on 21/08/2017.
 */

public class GeocodingResultCell extends RelativeLayout {

    TextView label, type;

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);

        int padding = (int)(5 * getResources().getDisplayMetrics().density);
        int width = params.width - 2 * padding;
        int split = params.height * 3 / 5;

        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(width, split);
        parameters.setMarginStart(padding);
        label.setLayoutParams(parameters);

        parameters = new RelativeLayout.LayoutParams(width, params.height);
        parameters.setMargins(padding, split, 0, 0);
        type.setLayoutParams(parameters);
    }

    public GeocodingResultCell(Context context) {
        super(context);

        label = new TextView(context);
        label.setGravity(Gravity.CENTER_VERTICAL);
        label.setTextColor(Color.WHITE);
        addView(label);

        type = new TextView(context);
        type.setGravity(Gravity.CENTER_VERTICAL);
        type.setTextColor(Color.LTGRAY);
        addView(type);
    }

    public void update(GeocodingResult result) {
        Address address = result.getAddress();

        label.setText(GeocodingUtils.getPrettyAddress(address));

        if (!address.getName().isEmpty()) {
            type.setText("Point of Interest");
        } else if (!address.getHouseNumber().isEmpty()) {
            type.setText("Address");
        } else if (!address.getStreet().isEmpty()) {
            type.setText("Street");
        } else if (!address.getNeighbourhood().isEmpty()) {
            type.setText("Neighbourhood");
        } else if (!address.getLocality().isEmpty()) {
            type.setText("Town/Village");
        } else if (!address.getCounty().isEmpty()) {
            type.setText("County");
        } else if (!address.getRegion().isEmpty()) {
            type.setText("Region");
        } else if (!address.getCountry().isEmpty()) {
            type.setText("Country");
        } else {
            type.setText("");
        }
    }
}
