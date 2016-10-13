package com.carto.cartomap.android.list;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.carto.cartomap.android.R;
import com.carto.cartomap.android.sections.cartojsapi.CountriesVisMapActivity;
import com.carto.cartomap.android.sections.cartojsapi.DotsVisMapActivity;
import com.carto.cartomap.android.sections.cartojsapi.FontsVisMapActivity;
import com.carto.cartomap.android.sections.header.CartoJSHeader;
import com.carto.cartomap.android.sections.header.ImportHeader;
import com.carto.cartomap.android.sections.header.MapsHeader;
import com.carto.cartomap.android.sections.header.SQLHeader;
import com.carto.cartomap.android.sections.header.TorqueHeader;
import com.carto.cartomap.android.sections.importapi.CartoRasterTileMapActivity;
import com.carto.cartomap.android.sections.mapsapi.CartoVectorTileMapActivity;
import com.carto.cartomap.android.sections.sqlapi.CartoSQLMapActivity;
import com.carto.cartomap.android.sections.sqlapi.SQLServiceActivity;
import com.carto.cartomap.android.sections.torqueapi.TorqueShipActivity;
import com.carto.cartomap.android.sections.importapi.CartoUTFGridMapActivity;
import com.carto.cartomap.android.util.Description;

public class LauncherListActivity extends ListActivity {

    private Class[] samples = {

            CartoJSHeader.class,
            CountriesVisMapActivity.class,
            DotsVisMapActivity.class,
            FontsVisMapActivity.class,

            ImportHeader.class,
            CartoRasterTileMapActivity.class,
            CartoUTFGridMapActivity.class,

            MapsHeader.class,
            CartoVectorTileMapActivity.class,

            SQLHeader.class,
            SQLServiceActivity.class,
            CartoSQLMapActivity.class,

            TorqueHeader.class,
            TorqueShipActivity.class,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_list);


        ListView lv = this.getListView();
        lv.setBackgroundColor(Color.BLACK);
        lv.setAdapter(new MapListAdapter(this, android.R.layout.two_line_list_item, getListItems()));
    }

    private MapListItem[] getListItems()
    {
        MapListItem[] items = new MapListItem[samples.length];

        for(int i = 0; i < samples.length; i++) {

            String name = samples[i].getSimpleName().replace("Activity", "");
            String description = "none";

            System.out.println("Name: " + name);
            java.lang.annotation.Annotation[] annotations = samples[i].getAnnotations();

            if (annotations.length > 0 && annotations[0] instanceof Description) {
                description = ((Description) annotations[0]).value();
            }

            System.out.println("Description: " + description);

            MapListItem item = new MapListItem();

            if (name.contains("Header")) {
                item.name = description;
                item.isHeader = true;
            } else {
                item.name = name;
                item.description = description;
            }

            items[i] = item;
        }

        return  items;
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {

        Intent myIntent = new Intent(LauncherListActivity.this, samples[position]);
        this.startActivity(myIntent);
    }
}
