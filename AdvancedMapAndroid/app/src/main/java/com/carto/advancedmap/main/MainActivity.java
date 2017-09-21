package com.carto.advancedmap.main;

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
import com.carto.advancedmap.model.Sample;
import com.carto.advancedmap.model.Samples;
import com.carto.advancedmap.shared.Colors;

import java.io.File;

/**
 * Shows list of demo Activities. This is the "main" of samples
 */

public class MainActivity extends ListActivity {

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

        setTitle("Advanced Map");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        System.gc();
    }

    private MapListItem[] getListItems() {

        MapListItem[] items = new MapListItem[Samples.LIST.length];

        for (int i = 0; i < Samples.LIST.length; i++) {

            Sample sample = Samples.LIST[i];

            String title = sample.title;
            String description = sample.description;

            MapListItem item = new MapListItem();

            item.name = title;
            item.description = description;

            items[i] = item;
        }

        return items;
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {

        Sample sample = Samples.LIST[position];

        String title = sample.title;
        String description = sample.description;

        Intent intent = new Intent(MainActivity.this, sample.activity);

        intent.putExtra("title", title);
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
