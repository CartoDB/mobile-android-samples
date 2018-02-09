package com.carto.advancedmap.baseclasses.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.carto.advanced.kotlin.components.popup.SlideInPopupHeader;
import com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent.PackageCell;
import com.carto.advancedmap.MapApplication;
import com.carto.advancedmap.baseclasses.views.PackageManagerBaseView;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.packagemanager.PackageManagerListener;
import com.carto.packagemanager.PackageStatus;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by aareundo on 21/08/2017.
 */

public class PackageManagerBaseActivity extends BaseActivity {

    public String getFolderName() {
        // This function should be overridden in a child class
        throw new UnsupportedOperationException();
    }

    public String getSource() {
        // This function should be overridden in a child class
        throw new UnsupportedOperationException();
    }

    public PackageManagerBaseView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create package manager
        File baseDirectory = getApplicationContext().getExternalFilesDir(null);
        File folder = new File(baseDirectory, getFolderName());

        if (!(folder.mkdirs() || folder.isDirectory())) {
            Log.e(MapApplication.LOG_TAG, "Could not create package folder!");
        }
        try {
            String source = getSource();
            String path = folder.getAbsolutePath();
            contentView.manager = new CartoPackageManager(source, path);
        }
        catch (IOException e) {
            Log.e(MapApplication.LOG_TAG, "Exception: " + e);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        contentView.manager.start();
        contentView.manager.setPackageManagerListener(new PackageManagerListener() {
            @Override
            public void onPackageListUpdated() {
                contentView.updatePackages();
            }

            @Override
            public void onPackageStatusChanged(String id, int version, PackageStatus status) {
                contentView.onStatusChange(id, status);
            }

            @Override
            public void onPackageUpdated(String id, int version) {
                contentView.downloadComplete();
            }
        });

        contentView.manager.startPackageListDownload();

        SlideInPopupHeader header = contentView.popup.getPopup().getHeader();
        header.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentView.onPopupBackClick();
            }
        });

        contentView.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentView.setPackageContent();
                contentView.popup.show();
            }
        });

        ListView list = contentView.packageContent.getList();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PackageCell cell = (PackageCell)view;
                contentView.onPackageClick(cell.getItem());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        contentView.manager.stop(false);
        contentView.manager.setPackageManagerListener(null);

        contentView.popup.getPopup().getHeader().getBackButton().setOnClickListener(null);

        contentView.downloadButton.setOnClickListener(null);

        contentView.packageContent.getList().setOnItemClickListener(null);
    }
}
