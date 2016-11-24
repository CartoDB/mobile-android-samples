package com.carto.advancedmap.sections.offlinemap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carto.advancedmap.baseactivities.BaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.core.BinaryData;
import com.carto.core.MapPos;
import com.carto.datasources.PackageManagerTileDataSource;
import com.carto.layers.VectorTileLayer;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.packagemanager.PackageErrorType;
import com.carto.packagemanager.PackageManagerListener;
import com.carto.packagemanager.PackageStatus;
import com.carto.styles.CompiledStyleSet;
import com.carto.ui.MapView;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;
import com.carto.vectortiles.MBVectorTileDecoder;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by aareundo on 10/11/16.
 */

@ActivityData(name = "Basic Package Manager", description = "Download a bounding box of London")
public class BasicPackageManagerActivity extends BaseActivity {

    BasicPackageManagerView contentView;

    CartoPackageManager manager;

    BoundingBox bbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        setTitle(title);
        getActionBar().setSubtitle(description);

        // Programmatically created view.
        contentView = new BasicPackageManagerView(this);
        setContentView(contentView);

        String folder = createPackageFolder("citypackages");

        try {
            manager = new CartoPackageManager("nutiteq.osm", folder);
        } catch (IOException e) {
            alert("Couldn't create package manager");
            finish();
        }

        manager.setPackageManagerListener(new PackageListener());

        // Custom convenience class to enhance readability
        bbox = new BoundingBox(-0.8164, 51.2382, 0.6406, 51.7401);
        String packaged = bbox.toString();

        // Package version has no real value when looking up the status of bbox
        if (manager.getLocalPackageStatus(packaged, 1) == null) {
            manager.startPackageDownload(packaged);
        } else {
            updateStatusLabel("Package downloaded");
            contentView.zoomTo(bbox.getCenter());
        }

        contentView.setBaseLayer(new PackageManagerTileDataSource(manager));
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

    private String createPackageFolder(String name) {
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

    private class BasicPackageManagerView extends RelativeLayout {

        public MapView mapView;

        public TextView statusLabel;

        public BasicPackageManagerView(Context context)
        {
            super(context);

            mapView = new MapView(context);
            mapView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            addView(mapView);

            // Initialize & style Status label
            statusLabel = new TextView(context);
            statusLabel.setTextColor(Color.BLACK);

            GradientDrawable background = new GradientDrawable();
            background.setCornerRadius(5);
            background.setColor(Color.argb(160, 255, 255, 255));
            statusLabel.setBackground(background);

            statusLabel.setGravity(Gravity.CENTER);
            statusLabel.setTypeface(Typeface.create("HelveticaNeue", Typeface.NORMAL));

            DisplayMetrics screen = getResources().getDisplayMetrics();

            int width = screen.widthPixels / 2;
            int height = width / 4;

            int x = screen.widthPixels / 2 - width / 2;
            int y = screen.heightPixels / 100;

            RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(width, height);
            parameters.setMargins(x, y, 0, 0);

            addView(statusLabel, parameters);
        }

        public void zoomTo(MapPos position)
        {
            mapView.setFocusPos(mapView.getOptions().getBaseProjection().fromWgs84(position), 0);
            mapView.setZoom(12, 2);
        }

        public void setBaseLayer(PackageManagerTileDataSource source)
        {
            // Create style set
            BinaryData styleBytes = AssetUtils.loadAsset("nutiteq-dark.zip");
            CompiledStyleSet style = new CompiledStyleSet(new ZippedAssetPackage(styleBytes));

            // Create Decoder
            MBVectorTileDecoder decoder = new MBVectorTileDecoder(style);

            VectorTileLayer layer = new VectorTileLayer(source, decoder);

            mapView.getLayers().add(layer);
        }
    }

    private class BoundingBox
    {
        public double minLat;

        public double minLon;

        public double maxLat;

        public double maxLon;

        public BoundingBox(double minLon, double minLat, double maxLon, double maxLat) {
            this.minLat = minLat;
            this.minLon = minLon;
            this.maxLat = maxLat;
            this.maxLon = maxLon;
        }
        public MapPos getCenter() {
            return new MapPos((maxLon + minLon) / 2, (maxLat + minLat) / 2);
        }

        public String toString()
        {
            return String.format(Locale.US, "bbox(" + minLon + "," + minLat + "," + maxLon + "," + maxLat + ")", null);
//            return String.format(Locale.US, "bbox(%4f,%4f,%4f,%4f)", minLon, minLat, maxLon, maxLat);
        }
    }
}
