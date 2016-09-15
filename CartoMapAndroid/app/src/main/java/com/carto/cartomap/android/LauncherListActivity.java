package com.carto.cartomap.android;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.carto.cartomap.android.map.CartoRasterTileActivity;
import com.carto.cartomap.android.map.CartoSQLActivity;
import com.carto.cartomap.android.map.CartoTorqueActivity;
import com.carto.cartomap.android.map.CartoUTFGridActivity;
import com.carto.cartomap.android.map.CartoVectorTileActivity;
import com.carto.cartomap.android.map.CartoVisJSONActivity;

public class LauncherListActivity extends ListActivity {

    // list of demos: MapActivity, ParameterSelectorActivity (can be null)
    // if parameter selector is given, then this is launched first to get a parameter (file path)
    private Object[][] samples = {
        { CartoVisJSONActivity.class, null },
        { CartoRasterTileActivity.class, null },
        { CartoSQLActivity.class, null },
        { CartoTorqueActivity.class, null },
        { CartoUTFGridActivity.class, null },
        { CartoVectorTileActivity.class, null }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_list);


        ListView lv = this.getListView();
        lv.setBackgroundColor(Color.BLACK);
        lv.setAdapter(new TwoLineArrayAdapter(this, android.R.layout.two_line_list_item, getListItems()));
    }

    private MapListItem[] getListItems()
    {
        MapListItem[] items = new MapListItem[samples.length];

        for(int i = 0; i < samples.length; i++) {

            Integer counter = i + 1;

            String name = ((Class<?>) samples[i][0]).getSimpleName().replace("Activity", "");
            String description = "none";

            java.lang.annotation.Annotation[] annotations = ((Class<?>) samples[i][0]).getAnnotations();

            if (annotations.length > 0 && annotations[0] instanceof Description) {
                description = ((Description) annotations[0]).value();
            }

            MapListItem item = new MapListItem() {};
            item.name = counter + ". " + name;
            item.description = description;

            items[i] = item;
        }

        return  items;
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {

        Intent myIntent = new Intent(LauncherListActivity.this, (Class<?>) samples[position][0]);
        this.startActivity(myIntent);
    }
}
