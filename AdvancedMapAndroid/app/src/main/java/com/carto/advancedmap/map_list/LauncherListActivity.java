package com.carto.advancedmap.map_list;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class LauncherListActivity extends ListActivity{

    // list of demos: MapActivity, ParameterSelectorActivity (can be null)
    // if parameter selector is given, then this is launched first to get a parameter (file path)
    private Object[][] samples = {
            { AerialMapActivity.class, null },
            { AnimatedRasterMapActivity.class, null },
            { CaptureActivity.class, null },
            { ClusteredRandomPointsActivity.class, null },
            { ClusteredGeoJsonActivity.class, null },
            { CustomRasterDataSourceActivity.class, null },
            { CustomPopupActivity.class, null },
            { GroundOverlayActivity.class, null },
            { MapListenerActivity.class, null },
            { MbtilesActivity.class, FilePicker.class },
            { MyLocationActivity.class, null },
            { PackageManagerActivity.class, null },
            { OfflineRoutingActivity.class, null },
            { OfflineVectorMapActivity.class, null },
            { Overlays2DActivity.class, null },
            { Overlays3DActivity.class, null },
            { RasterOverlayActivity.class, null },
            { PinMapActivity.class, null },
            { WmsMapActivity.class, null }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);

        ListView lv = this.getListView();
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getStringArray()));
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
        if(fileName != null && className != null){
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
