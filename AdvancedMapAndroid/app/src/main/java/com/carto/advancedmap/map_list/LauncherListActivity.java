package com.carto.advancedmap.map_list;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Annotation;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.MapListItem;
import com.carto.advancedmap.TwoLineArrayAdapter;
import com.carto.advancedmap.map.AerialMapActivity;
import com.carto.advancedmap.map.AnimatedRasterMapActivity;
import com.carto.advancedmap.map.CaptureActivity;
import com.carto.advancedmap.map.ClusteredGeoJsonActivity;
import com.carto.advancedmap.map.ClusteredRandomPointsActivity;
import com.carto.advancedmap.map.CustomPopupActivity;
import com.carto.advancedmap.map.CustomRasterDataSourceActivity;
import com.carto.advancedmap.map.GroundOverlayActivity;
import com.carto.advancedmap.map.MapListenerActivity;
import com.carto.advancedmap.map.MbtilesActivity;
import com.carto.advancedmap.map.MyLocationActivity;
import com.carto.advancedmap.map.OfflineRoutingActivity;
import com.carto.advancedmap.map.OfflineVectorMapActivity;
import com.carto.advancedmap.map.Overlays2DActivity;
import com.carto.advancedmap.map.Overlays3DActivity;
import com.carto.advancedmap.map.PinMapActivity;
import com.carto.advancedmap.map.RasterOverlayActivity;
import com.carto.advancedmap.map.WmsMapActivity;
import com.carto.advancedmap.R;
import com.carto.filepicker.FilePicker;
import com.carto.filepicker.FilePickerActivity;

/**
 * Shows list of demo Activities. Enables to open pre-launch activity for file picking.
 * This is the "main" of samples
 */

public class LauncherListActivity extends ListActivity {

    // list of demos: MapActivity, ParameterSelectorActivity (can be null)
    // if parameter selector is given, then this is launched first to get a parameter (file path)
    private Object[][] samples = {
            { PinMapActivity.class, null },
            { Overlays2DActivity.class, null },
            { MapListenerActivity.class, null },
            { MyLocationActivity.class, null },
            { PackageManagerActivity.class, null },
            { Overlays3DActivity.class, null },
            { OfflineRoutingActivity.class, null },
            { AerialMapActivity.class, null },
            { AnimatedRasterMapActivity.class, null },
            { CaptureActivity.class, null },
            { ClusteredRandomPointsActivity.class, null },
            { ClusteredGeoJsonActivity.class, null },
            { CustomRasterDataSourceActivity.class, null },
            { CustomPopupActivity.class, null },
            { GroundOverlayActivity.class, null },
            { MbtilesActivity.class, FilePicker.class },
            { OfflineVectorMapActivity.class, null },
            { RasterOverlayActivity.class, null },
            { WmsMapActivity.class, null }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);

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
        if (samples[position][1] != null) {

            try {

                Intent myIntent = new Intent(LauncherListActivity.this, (Class<?>) samples[position][1]);

                Class<?> activityToRun = (Class<?>) samples[position][0];
                FilePickerActivity activityInstance = (FilePickerActivity) activityToRun.newInstance();

                FilePicker.setFileSelectMessage(activityInstance.getFileSelectMessage());
                FilePicker.setFileDisplayFilter(activityInstance.getFileFilter());

                Bundle b = new Bundle();
                b.putString("class", ((Class<?>) samples[position][0]).getName());
                myIntent.putExtras(b);
                startActivityForResult(myIntent, 1);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        } else {
            Intent myIntent = new Intent(LauncherListActivity.this,(Class<?>) samples[position][0]);
            this.startActivity(myIntent);
        }
    }
    
    
    // gets fileName from FilePicker and starts Map Activity with fileName as parameter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null){
            return;
        }

        String fileName = data.getStringExtra("selectedFile");
        String className = data.getStringExtra("class");

        if (fileName != null && className != null) {
            try {
                Intent myIntent = new Intent(LauncherListActivity.this,
                            Class.forName(className));
    
                Bundle b = new Bundle();
                b.putString("selectedFile", fileName);
                myIntent.putExtras(b);
                this.startActivity(myIntent);
            
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
}
