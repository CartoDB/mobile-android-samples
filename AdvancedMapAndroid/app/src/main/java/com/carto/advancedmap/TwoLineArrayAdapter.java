package com.carto.advancedmap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by aareundo on 31/08/16.
 */
public class TwoLineArrayAdapter extends ArrayAdapter<MapListItem> {

    private int mListItemLayoutResId;

    MapListItem[] items;

    public TwoLineArrayAdapter(Context context, int listItemLayoutResourceId, MapListItem[] ts) {
        super(context, listItemLayoutResourceId, ts);

        mListItemLayoutResId = listItemLayoutResourceId;
    }

    @Override
    public android.view.View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = convertView;

        if (null == convertView) {
            listItemView = inflater.inflate(mListItemLayoutResId, parent, false);
        }

        // The ListItemLayout must use the standard text item IDs.
        TextView lineOneView = (TextView)listItemView.findViewById(android.R.id.text1);
        TextView lineTwoView = (TextView)listItemView.findViewById(android.R.id.text2);

        // Default color changed in android 7.0, need to set it to white for newer versions.
        lineOneView.setTextColor(Color.WHITE);
        lineTwoView.setTextColor(Color.WHITE);

        MapListItem item = getItem(position);

        lineOneView.setText(item.name);
        lineTwoView.setText(item.description);

        return listItemView;
    }
}
