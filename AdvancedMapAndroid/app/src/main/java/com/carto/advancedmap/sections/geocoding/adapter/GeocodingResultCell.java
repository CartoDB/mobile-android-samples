package com.carto.advancedmap.sections.geocoding.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
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

        label.setText(GeocodingUtils.getPrettyAddres(address));

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
