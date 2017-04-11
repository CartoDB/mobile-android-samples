package com.carto.advancedmap.shared.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.carto.advancedmap.sections.offlinemap.BasicPackageManagerActivity;
import com.carto.advancedmap.shared.views.BasicPackageManagerView;
import com.carto.advancedmap.utils.BoundingBox;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.packagemanager.PackageErrorType;
import com.carto.packagemanager.PackageManagerListener;
import com.carto.packagemanager.PackageStatus;

import java.io.File;
import java.io.IOException;

/**
 * Created by aareundo on 14/03/17.
 */

public class BoundingBoxActivity extends BaseActivity {

    protected BasicPackageManagerView contentView;

    protected CartoPackageManager manager;

    protected BoundingBox bbox;

    protected String createPackageFolder() throws Exception {
        throw new Exception("Please override this method");
    }

    protected CartoPackageManager getPackageManager(String folder) throws Exception  {
        throw new Exception("Please override this method");
    }

    protected BoundingBox getBoundingBox() throws Exception  {
        throw new Exception("Please override this method");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        setTitle(title);
        getActionBar().setSubtitle(description);

        String folder = null;
        try {
            folder = createPackageFolder();
        } catch (Exception e) {
            e.printStackTrace();
            // Should never reach this block, methods should be overridden in child class.
            // I, in fact, would like to have this crash the app.
        }

        try {
            manager = getPackageManager(folder);
        } catch (Exception e) {
            e.printStackTrace();
            // Should never reach this block, methods should be overridden in child class.
            // I, in fact, would like to have this crash the app.
        }

        if (manager == null) {
            alert("Couldn't create package manager");
            finish();
        }

        // Programmatically created view.
        contentView = new BasicPackageManagerView(this, manager);
        setContentView(contentView);

        manager.setPackageManagerListener(new PackageListener());

        // Custom convenience class to enhance readability
        try {
            bbox = getBoundingBox();
        } catch (Exception e) {
            e.printStackTrace();
            // Should never reach this block, methods should be overridden in child class.
            // I, in fact, would like to have this crash the app.
        }

        String packaged = bbox.toString();

        // Package version has no real value when looking up the status of bbox
        if (manager.getLocalPackageStatus(packaged, 1) == null) {
            manager.startPackageDownload(packaged);
        } else {
            updateStatusLabel("Package downloaded");
            contentView.zoomTo(bbox.getCenter());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.start();
    }

    @Override
    protected void onPause() {
        manager.stop(true);
        super.onPause();
    }

    private void updateStatusLabel(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contentView.statusLabel.setText(value);
            }
        });
    }

    protected String createPackageFolder(String name) {
        File packageFolder = new File(getApplicationContext().getExternalFilesDir(null), name);

        if (!packageFolder.isDirectory()) {
            packageFolder.mkdirs();
        }

        return packageFolder.getAbsolutePath();
    }

    private class PackageListener extends PackageManagerListener {
        @Override
        public void onPackageListUpdated() {
            // This basic sample does not download lists
        }

        @Override
        public void onPackageListFailed() {
            // This basic sample does not download lists
        }

        @Override
        public void onPackageStatusChanged(String id, int version, PackageStatus status) {
            updateStatusLabel("Progress: " + status.getProgress() + "%");
        }

        @Override
        public void onPackageCancelled(String id, int version) {
            // Download cannot be cancelled in our basic sample.
            // See advanced sample for that functionality
        }

        @Override
        public void onPackageUpdated(String id, int version) {
            updateStatusLabel("Downloaded Complete");
            contentView.zoomTo(bbox.getCenter());
        }

        @Override
        public void onPackageFailed(String id, int version, PackageErrorType errorType) {
            updateStatusLabel("Failed: " + errorType);
        }
    }
}
