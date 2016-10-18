package com.carto.advancedmap.list;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.carto.advancedmap.sections.basemap.BaseMapsActivity;
import com.carto.advancedmap.sections.header.BaseMapHeader;
import com.carto.advancedmap.sections.header.OfflineMapHeader;
import com.carto.advancedmap.sections.header.OtherMapHeader;
import com.carto.advancedmap.sections.header.OverlayDataSourcesHeader;
import com.carto.advancedmap.sections.header.VectorObjectsHeader;
import com.carto.advancedmap.sections.offlinemap.packagemanager.PackageManagerActivity;
import com.carto.advancedmap.util.Description;
import com.carto.advancedmap.sections.other.CaptureActivity;
import com.carto.advancedmap.sections.other.ClusteredGeoJsonActivity;
import com.carto.advancedmap.sections.other.CustomPopupActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomRasterDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.GroundOverlayActivity;
import com.carto.advancedmap.sections.other.MyLocationActivity;
import com.carto.advancedmap.sections.other.OfflineRoutingActivity;
import com.carto.advancedmap.sections.offlinemap.BundledMBTilesActivity;
import com.carto.advancedmap.sections.vectorobjects.OverlaysActivity;
import com.carto.advancedmap.sections.overlaydatasources.WmsMapActivity;
import com.carto.advancedmap.R;

/**
 * Shows list of demo Activities. Enables to open pre-launch activity for file picking.
 * This is the "main" of samples
 */

public class LauncherListActivity extends ListActivity {

    public Class[] samples = {

            BaseMapHeader.class,
            BaseMapsActivity.class,

            OfflineMapHeader.class,
            PackageManagerActivity.class,
            BundledMBTilesActivity.class,

            OverlayDataSourcesHeader.class,
            CustomRasterDataSourceActivity.class,
            GroundOverlayActivity.class,
            WmsMapActivity.class,

            VectorObjectsHeader.class,
            OverlaysActivity.class,

            OtherMapHeader.class,
            CaptureActivity.class,
            ClusteredGeoJsonActivity.class,
            CustomPopupActivity.class,
            MyLocationActivity.class,
            OfflineRoutingActivity.class
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
        lv.setAdapter(new MapListAdapter(this, android.R.layout.two_line_list_item, getListItems()));
    }

    private MapListItem[] getListItems()
    {
        MapListItem[] items = new MapListItem[samples.length];

        for(int i = 0; i < samples.length; i++) {

            String name = samples[i].getSimpleName().replace("Activity", "");
            String description = "none";

            java.lang.annotation.Annotation[] annotations = samples[i].getAnnotations();

            if (annotations.length > 0 && annotations[0] instanceof Description) {
                description = ((Description) annotations[0]).value();
            }

            MapListItem item = new MapListItem();

            if (name.contains("Header")) {
                item.name = description;
                item.isHeader = true;
            } else {
                item = new MapListMap();
                item.name = name;
                item.description = description;
            }

            items[i] = item;
        }

        return  items;
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {

        Class sample = samples[position];

        if (sample.getSimpleName().contains("Header")) {
            return;
        }

        Intent myIntent = new Intent(LauncherListActivity.this, sample);
        this.startActivity(myIntent);
    }

    // Filler class to differentiate between clickable Map and Header for Espresso tests
    public class MapListMap extends MapListItem {

    }

}
