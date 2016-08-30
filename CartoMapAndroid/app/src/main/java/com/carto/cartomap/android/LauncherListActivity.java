package com.carto.cartomap.android;

import android.app.ListActivity;
import android.content.Intent;
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
        { CartoRasterTileActivity.class, null },
        { CartoSQLActivity.class, null },
        { CartoTorqueActivity.class, null },
        { CartoUTFGridActivity.class, null },
        { CartoVectorTileActivity.class, null },
        { CartoVisJSONActivity.class, null },
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_list);


        ListView lv = this.getListView();
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getStringArray()));
    }

    private String[] getStringArray() {
        String[] sampleNames = new String[samples.length];

        for(int i=0; i < samples.length; i++) {

            Integer counter = i + 1;

            String name = ((Class<?>) samples[i][0]).getSimpleName();
            name = name.replace("Carto", "").replace("Activity", "");

            sampleNames[i] = counter + ". " + name;
        }

        return sampleNames;
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {

        Intent myIntent = new Intent(LauncherListActivity.this, (Class<?>) samples[position][0]);
        this.startActivity(myIntent);
    }
}
