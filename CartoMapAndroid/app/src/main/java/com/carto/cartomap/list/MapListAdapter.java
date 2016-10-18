package com.carto.cartomap.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by aareundo on 31/08/16.
 */
public class MapListAdapter extends ArrayAdapter<MapListItem> {

    Context context;

    public MapListAdapter(Context context, int listItemLayoutResourceId, MapListItem[] ts) {
        super(context, listItemLayoutResourceId, ts);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MapListRow row = (MapListRow)convertView;

        MapListItem item = getItem(position);

        if (row == null) {
            row = new MapListRow(context, item);
        } else {
            row.update(item);
        }

        return row;
    }
}
