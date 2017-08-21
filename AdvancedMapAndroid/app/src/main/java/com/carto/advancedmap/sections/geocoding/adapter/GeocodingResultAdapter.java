package com.carto.advancedmap.sections.geocoding.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.carto.geocoding.GeocodingResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aareundo on 21/08/2017.
 */

public class GeocodingResultAdapter extends ArrayAdapter<GeocodingResult> {

    List<GeocodingResult> items = new ArrayList<GeocodingResult>();
    public int width;

    public GeocodingResultAdapter(Context context) {
        super(context, -1);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public GeocodingResult getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        GeocodingResultCell cell;

        GeocodingResult item = items.get(position);

        if (convertView == null) {
            cell = new GeocodingResultCell(getContext());

            int height = (int)(40 * getContext().getResources().getDisplayMetrics().density);
            cell.setLayoutParams(new AbsListView.LayoutParams(width, height));
        } else {
            cell = (GeocodingResultCell)convertView;
        }

        cell.update(item);

        return cell;
    }
}
