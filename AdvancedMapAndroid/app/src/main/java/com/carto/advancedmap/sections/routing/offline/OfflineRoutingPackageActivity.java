package com.carto.advancedmap.sections.routing.offline;

import android.os.Bundle;
import android.view.MenuItem;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.advancedmap.sections.routing.BaseRoutingActivity;
import com.carto.advancedmap.shared.packages.Package;
import com.carto.advancedmap.shared.packages.PackageAdapter;
import com.carto.advancedmap.utils.Sources;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.packagemanager.PackageErrorType;
import com.carto.packagemanager.PackageInfo;
import com.carto.packagemanager.PackageInfoVector;
import com.carto.packagemanager.PackageManagerListener;
import com.carto.packagemanager.PackageStatus;
import com.carto.routing.PackageManagerValhallaRoutingService;
import com.carto.ui.MapView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by aareundo on 16/12/16.
 */

@ActivityData(name = "Offline Routing (package)", description = "Offline Routing where country packages are downloaded")
public class OfflineRoutingPackageActivity extends BaseRoutingActivity {

    CartoPackageManager manager;

    PackageListener listener;
    PackageAdapter adapter;

    OfflineRoutingView contentView;

    ArrayList<Package> packages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        alert("This sample uses an online map, but downloads routing packages");

        String folder = createFolder();

        try {
            manager = new CartoPackageManager(Sources.ROUTING_TAG + Sources.OFFLINE_ROUTING, folder);
        } catch (IOException e) {
            alert(e.getMessage());
            return;
        }

        packages = new ArrayList<>();
        adapter = new PackageAdapter(this, com.carto.advancedmap.R.layout.package_item_row, packages, manager);

        contentView = new OfflineRoutingView(this, adapter);

        super.onCreate(savedInstanceState);

        setContentView(contentView);

        setService(new PackageManagerValhallaRoutingService(manager));
        alert("Click on the menu to see a list of countries that can be downloaded");

        listener = new PackageListener();
        manager.setPackageManagerListener(listener);
        manager.start();
        manager.startPackageListDownload();
    }

    @Override
    protected MapView getMapView() {
        return contentView.mapView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (contentView.menu.isVisible()) {
                contentView.menu.hide();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (contentView.menu.isVisible()) {
            contentView.menu.hide();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        listener = null;
        manager.stop(true);
    }

    String createFolder() {
        // Create PackageManager instance for dealing with offline packages
        File folder = new File(getApplicationContext().getExternalFilesDir(null), "routingpackages");

        if (!(folder.mkdirs() || folder.isDirectory())) {
            return null;
        }

        return folder.getAbsolutePath();
    }

    public void updatePackages() {
        packages.clear();
        packages.addAll(getPackages());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void addBaseLayer() {
        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(BaseMapsView.DEFAULT_STYLE);
        contentView.mapView.getLayers().add(layer);
    }

    protected ArrayList<Package> getPackages() {

        String language = "en";
        ArrayList<Package> packages = new ArrayList<>();

        PackageInfoVector vector = manager.getServerPackages();

        for (int i = 0; i < vector.size(); i++)
        {
            PackageInfo info = vector.get(i);

            String name = info.getNames(language).get(0);
            System.out.println("Name: " + name);
            String[] split = name.split("/");
            int dashCount = info.getPackageId().split("-").length - 1;

            // If package id contains -, then it's a subdivision (province, county, oblast etc.) of a country,
            // add the country as well as the subdivision.

            // Routing packages also contain a dash (-routing).
            // Check if it contains two dashes. Then it's a subdivision
            if (dashCount == 2 && split.length > 2)
            {
                name = split[split.length - 2] + ", " + split[split.length - 1];
            }
            else {
                name = split[split.length - 1];
            }

            PackageStatus status = manager.getLocalPackageStatus(info.getPackageId(), -1);
            Package pkg = new Package(name, info, status);

            packages.add(pkg);
        }

        Collections.sort(packages, new Package.PackageComparator());
        return packages;
    }

    void updatePackage(final String id) {

        // Try to find the package that needs to be updated
        for (int i = 0; i < packages.size(); i++) {
            final Package pkg = packages.get(i);

            if (id.equals(pkg.id)) {
                PackageStatus status = manager.getLocalPackageStatus(id, -1);
                pkg.status = status;
                packages.set(i, pkg);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updatePackage(pkg);
                    }
                });

            }
        }
    }

    /**
     * Listener for package manager events.
     */
    public class PackageListener extends PackageManagerListener {

        @Override
        public void onPackageListUpdated() {
            updatePackages();
        }

        @Override
        public void onPackageListFailed() {
            updatePackages();
            alert("Failed to download package list");
        }

        @Override
        public void onPackageStatusChanged(String id, int version, PackageStatus status) {
            updatePackage(id);
        }

        @Override
        public void onPackageCancelled(String id, int version) {
            updatePackage(id);
        }

        @Override
        public void onPackageUpdated(String id, int version) {
            updatePackage(id);
        }

        @Override
        public void onPackageFailed(String id, int version, PackageErrorType errorType) {
            updatePackage(id);
            alert("Failed to download package " + id + "(v." + version + ")");
        }
    }

}
