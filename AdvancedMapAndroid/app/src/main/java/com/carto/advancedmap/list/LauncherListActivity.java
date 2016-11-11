package com.carto.advancedmap.list;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.carto.advancedmap.sections.basemap.BaseMapActivity;
import com.carto.advancedmap.sections.offlinemap.BasicPackageManagerActivity;
import com.carto.advancedmap.sections.offlinemap.advancedpackagemanager.AdvancedPackageManagerActivity;
import com.carto.advancedmap.sections.vectorobjects.ClusteredMarkersActivity;
import com.carto.advancedmap.sections.vectorobjects.VectorObjectEditingActivity;
import com.carto.advancedmap.sections.header.BaseMapHeader;
import com.carto.advancedmap.sections.header.OfflineMapHeader;
import com.carto.advancedmap.sections.header.OtherMapHeader;
import com.carto.advancedmap.sections.header.OverlayDataSourcesHeader;
import com.carto.advancedmap.sections.header.VectorObjectsHeader;
import com.carto.advancedmap.sections.other.CaptureActivity;
import com.carto.advancedmap.sections.other.CustomPopupActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomRasterDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.GroundOverlayActivity;
import com.carto.advancedmap.sections.other.GPSLocationActivity;
import com.carto.advancedmap.sections.other.OfflineRoutingActivity;
import com.carto.advancedmap.sections.offlinemap.BundledMapActivity;
import com.carto.advancedmap.sections.vectorobjects.OverlaysActivity;
import com.carto.advancedmap.sections.overlaydatasources.WmsMapActivity;
import com.carto.advancedmap.R;

/**
 * Shows list of demo Activities. This is the "main" of samples
 */

public class LauncherListActivity extends ListActivity {

    public Class[] samples = {

            BaseMapHeader.class,
            BaseMapActivity.class,

            OfflineMapHeader.class,
//            BasicPackageManagerActivity.class,
            AdvancedPackageManagerActivity.class,
            BundledMapActivity.class,

            OverlayDataSourcesHeader.class,
            CustomRasterDataSourceActivity.class,
            GroundOverlayActivity.class,
            WmsMapActivity.class,

            VectorObjectsHeader.class,
            ClusteredMarkersActivity.class,
            OverlaysActivity.class,
            VectorObjectEditingActivity.class,

            OtherMapHeader.class,
            CaptureActivity.class,
            CustomPopupActivity.class,
            GPSLocationActivity.class,
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

        setTitle("Advanced Mobile Samples");
    }

    private MapListItem[] getListItems()
    {
        MapListItem[] items = new MapListItem[samples.length];

        for(int i = 0; i < samples.length; i++) {

            String name = "";
            String description = "";

            java.lang.annotation.Annotation[] annotations = samples[i].getAnnotations();

            if (annotations.length > 0 && annotations[0] instanceof ActivityData) {
                name = ((ActivityData) annotations[0]).name();
                description = ((ActivityData) annotations[0]).description();
            }

            MapListItem item = new MapListItem();


            if (description.equals("")) {
                item.name = name;
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

        ActivityData data = ((ActivityData) sample.getAnnotations()[0]);
        String name = data.name();
        String description = data.description();

        if (description.equals("")) {
            // Headers don't have descriptions and aren't clickable
            return;
        }

        Intent intent = new Intent(LauncherListActivity.this, sample);

        intent.putExtra("title", name);
        intent.putExtra("description", description);

        this.startActivity(intent);
    }

    // Filler class to differentiate between clickable Map and Header for Espresso tests
    public class MapListMap extends MapListItem {

    }

}
