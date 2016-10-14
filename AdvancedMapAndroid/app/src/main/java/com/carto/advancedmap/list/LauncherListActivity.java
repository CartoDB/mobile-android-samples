package com.carto.advancedmap.list;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.MapListItem;
import com.carto.advancedmap.TwoLineArrayAdapter;
import com.carto.advancedmap.mapsamples.AerialMapActivity;
import com.carto.advancedmap.mapsamples.AnimatedRasterMapActivity;
import com.carto.advancedmap.mapsamples.CaptureActivity;
import com.carto.advancedmap.mapsamples.ClusteredGeoJsonActivity;
import com.carto.advancedmap.mapsamples.ClusteredRandomPointsActivity;
import com.carto.advancedmap.mapsamples.CustomPopupActivity;
import com.carto.advancedmap.mapsamples.CustomRasterDataSourceActivity;
import com.carto.advancedmap.mapsamples.GroundOverlayActivity;
import com.carto.advancedmap.mapsamples.InteractivityMapActivity;
import com.carto.advancedmap.mapsamples.MapListenerActivity;
import com.carto.advancedmap.mapsamples.MyLocationActivity;
import com.carto.advancedmap.mapsamples.OfflineRoutingActivity;
import com.carto.advancedmap.mapsamples.OfflineVectorMapActivity;
import com.carto.advancedmap.mapsamples.Overlays2DActivity;
import com.carto.advancedmap.mapsamples.Overlays3DActivity;
import com.carto.advancedmap.mapsamples.PinMapActivity;
import com.carto.advancedmap.mapsamples.RasterOverlayActivity;
import com.carto.advancedmap.mapsamples.WmsMapActivity;
import com.carto.advancedmap.R;

/**
 * Shows list of demo Activities. Enables to open pre-launch activity for file picking.
 * This is the "main" of samples
 */

public class LauncherListActivity extends ListActivity {

    public Class[] samples = {
            PinMapActivity.class,
            Overlays2DActivity.class,
            MapListenerActivity.class,
            MyLocationActivity.class,
            PackageManagerActivity.class,
            Overlays3DActivity.class,
            InteractivityMapActivity.class,
            OfflineRoutingActivity.class,
            AerialMapActivity.class,
            AnimatedRasterMapActivity.class,
            CaptureActivity.class,
            ClusteredRandomPointsActivity.class,
            ClusteredGeoJsonActivity.class,
            CustomRasterDataSourceActivity.class,
            CustomPopupActivity.class,
            GroundOverlayActivity.class,
            OfflineVectorMapActivity.class,
            RasterOverlayActivity.class,
            WmsMapActivity.class,
    };

    public void unlockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

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

            String name = samples[i].getSimpleName().replace("Activity", "");
            String description = "none";

            java.lang.annotation.Annotation[] annotations = samples[i].getAnnotations();

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

        Intent myIntent = new Intent(LauncherListActivity.this, samples[position]);
        this.startActivity(myIntent);
    }
    
}
