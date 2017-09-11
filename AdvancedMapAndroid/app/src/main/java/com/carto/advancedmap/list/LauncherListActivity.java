package com.carto.advancedmap.list;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.carto.advancedmap.R;
import com.carto.advancedmap.sections.basemap.BaseMapActivity;
import com.carto.advancedmap.sections.geocoding.offline.GeoPackageDownloadActivity;
import com.carto.advancedmap.sections.geocoding.online.OnlineGeocodingActivity;
import com.carto.advancedmap.sections.geocoding.online.OnlineReverseGeocodingActivity;
import com.carto.advancedmap.sections.geocoding.offline.ReverseGeoPackageDownloadActivity;
import com.carto.advancedmap.sections.header.BaseMapHeader;
import com.carto.advancedmap.sections.header.GeocodingHeader;
import com.carto.advancedmap.sections.header.OfflineMapHeader;
import com.carto.advancedmap.sections.header.OtherMapHeader;
import com.carto.advancedmap.sections.header.OverlayDataSourcesHeader;
import com.carto.advancedmap.sections.header.RoutingHeader;
import com.carto.advancedmap.sections.header.VectorObjectsHeader;
import com.carto.advancedmap.sections.offlinemap.BasicPackageManagerActivity;
import com.carto.advancedmap.sections.offlinemap.BundledMapActivity;
import com.carto.advancedmap.sections.offlinemap.advancedpackagemanager.AdvancedPackageManagerActivity;
import com.carto.advancedmap.sections.other.CaptureActivity;
import com.carto.advancedmap.sections.other.CustomPopupActivity;
import com.carto.advancedmap.sections.other.GPSLocationActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomRasterDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomVectorDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.GroundOverlayActivity;
import com.carto.advancedmap.sections.overlaydatasources.WmsMapActivity;
import com.carto.advancedmap.sections.routing.OfflineRoutingBBoxActivity;
import com.carto.advancedmap.sections.routing.OnlineRoutingActivity;
import com.carto.advancedmap.sections.routing.offline.OfflineRoutingPackageActivity;
import com.carto.advancedmap.sections.vectorobjects.ClusteredMarkersActivity;
import com.carto.advancedmap.sections.vectorobjects.OverlaysActivity;
import com.carto.advancedmap.sections.vectorobjects.VectorObjectEditingActivity;
import com.carto.advancedmap.shared.Colors;

import java.io.File;

/**
 * Shows list of demo Activities. This is the "main" of samples
 */

public class LauncherListActivity extends ListActivity {

    public Class[] samples = {

            BaseMapHeader.class,
            BaseMapActivity.class,

            OfflineMapHeader.class,
            BasicPackageManagerActivity.class,
            AdvancedPackageManagerActivity.class,
            BundledMapActivity.class,

            RoutingHeader.class,
            OnlineRoutingActivity.class,
            OfflineRoutingPackageActivity.class,
            OfflineRoutingBBoxActivity.class,

            GeocodingHeader.class,
            OnlineReverseGeocodingActivity.class,
            OnlineGeocodingActivity.class,
            ReverseGeoPackageDownloadActivity.class,
            GeoPackageDownloadActivity.class,

            OverlayDataSourcesHeader.class,
            CustomRasterDataSourceActivity.class,
            CustomVectorDataSourceActivity.class,
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
    };

    public void unlockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable background = new ColorDrawable(Colors.ACTION_BAR);
        getActionBar().setBackgroundDrawable(background);

        setContentView(R.layout.list);

        ListView lv = this.getListView();
        lv.setBackgroundColor(Color.BLACK);
        lv.setAdapter(new MapListAdapter(this, android.R.layout.two_line_list_item, getListItems()));

        setTitle("Advanced Mobile Samples");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        System.gc();
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
                // Filler class for tests
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

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    // mkFolder() with permission logic for Espresso tests
    public int mkFolder(String folderName) { // make a folder under Environment.DIRECTORY_DCIM
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Log.d("myAppName", "Error: external storage is unavailable");
            return 0;
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("myAppName", "Error: external storage is read only.");
            return 0;
        }
        Log.d("myAppName", "External storage is not read only or unavailable");

        // request permission when it is not granted.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("myAppName", "permission:WRITE_EXTERNAL_STORAGE: NOT granted!");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),folderName);

        int result = 0;
        if (folder.exists()) {
            Log.d("myAppName","folder exist:"+folder.toString());
            result = 2; // folder exist
        } else {
            try {
                if (folder.mkdir()) {
                    Log.d("myAppName", "folder created:" + folder.toString());
                    result = 1; // folder created
                } else {
                    Log.d("myAppName", "creat folder fails:" + folder.toString());
                    result = 0; // creat folder fails
                }
            }catch (Exception ecp){
                ecp.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request
        }
    }

    // Filler class to differentiate between clickable Map and Header for Espresso tests
    public class MapListMap extends MapListItem {

    }

}
