package com.carto.advancedmap.baseclasses.views;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.carto.advanced.kotlin.components.PopupButton;
import com.carto.advanced.kotlin.components.ProgressLabel;
import com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent.PackagePopupContent;
import com.carto.advanced.kotlin.sections.base.views.BaseView;
import com.carto.advancedmap.R;
import com.carto.advancedmap.model.Cities;
import com.carto.advancedmap.model.City;
import com.carto.advancedmap.model.Package;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.packagemanager.PackageInfo;
import com.carto.packagemanager.PackageInfoVector;
import com.carto.packagemanager.PackageStatus;

import java.util.ArrayList;

/**
 * Created by aareundo on 21/09/2017.
 */

public class PackageManagerBaseView extends MapBaseView {

    public CartoPackageManager manager;

    public PopupButton downloadButton;

    public PackagePopupContent packageContent;

    public ProgressLabel progressLabel;

    public PackageManagerBaseView(Context context) {
        super(context);

        downloadButton = new PopupButton(context, R.drawable.icon_global);
        addButton(downloadButton);

        progressLabel = new ProgressLabel(context);
        addView(progressLabel);

        packageContent = new PackagePopupContent(context);

        progressLabel = new ProgressLabel(context);
        addView(progressLabel);

        setMainViewFrame();
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();

        if (progressLabel == null) {
            // Progress Label will be null when MapBaseView calls layoutSubviews
            return;
        }

        int w = getFrame().getWidth();
        int h = bottomLabelHeight;
        int x = getFrame().getWidth();
        int y = getFrame().getHeight() - h;

        progressLabel.setFrame(x, y, w, h);
    }

    public void setPackageContent() {
        popup.getPopup().getHeader().setText("SELECT A PACKAGE");
        setContent(packageContent);
    }

    public void setContent(BaseView content) {
        int x = 0;
        int y = popup.getPopup().getHeader().getFrame().getHeight();
        int w = popup.getPopup().getFrame().getWidth();
        int h = popup.getPopup().getFrame().getHeight() - y;

        if (content.getParent() == null) {
            popup.getPopup().addView(content);
        } else {
            popup.getPopup().bringChildToFront(content);
        }

        content.setFrame(x, y, w, h);
    }

    private ArrayList<Package> getPackages() {

        ArrayList<Package> list = new ArrayList<>();

        if (currentFolder.equals(Package.CUSTOM_REGION_FOLDER_NAME + "/")) {
            ArrayList<Package> custom = getCustomRegionPackages();
            list.addAll(custom);
            return list;
        }

        if (currentFolder.isEmpty()) {
            list.add(getCustomRegionFolder());
        }

        PackageInfoVector infoVector = manager.getServerPackages();

        for (int i = 0; i < infoVector.size(); i++) {

            PackageInfo info = infoVector.get(i);

            // Get the list of names for this package. Each package may have multiple names,
            // packages are grouped using '/' as a separator, so the the full name for Sweden
            // is "Europe/Northern Europe/Sweden". We display packages as a tree, so we need
            // to extract only relevant packages belonging to the current folder.

            String name = info.getName();

            if (!name.startsWith(currentFolder)) {
                continue; // belongs to a different folder, so ignore
            }

            name = name.substring(currentFolder.length());
            int index = name.indexOf('/');
            Package pkg;

            if (index == -1) {
                // This is actual package
                PackageStatus status = manager.getLocalPackageStatus(info.getPackageId(), -1);
                pkg = new Package(name, info, status);
            } else {
                // This is package group
                name = name.substring(0, index);

                // Try n' find an existing package from the list.
                ArrayList<Package> existingPackages = new ArrayList<>();

                for (Package existingPackage : list) {
                    if (existingPackage.name.equals(name)) {
                        existingPackages.add(existingPackage);
                    }
                }

                if (existingPackages.size() == 0) {
                    // If there are none, add a package group if we don't have an existing list item
                    pkg = new Package(name, null, null);
                } else if (existingPackages.size() == 1 && existingPackages.get(0).info != null) {

                    // Sometimes we need to add two labels with the same name.
                    // One a downloadable package and the other pointing to a list of said country's counties,
                    // such as with Spain, Germany, France, Great Britain

                    // If there is one existing package and its info isn't null,
                    // we will add a "parent" package containing subpackages (or package group)
                    pkg = new Package(name, null, null);

                } else {
                    // Shouldn't be added, as both cases are accounted for
                    continue;
                }
            }

            list.add(pkg);
        }

        return list;
    }

    public String currentFolder = ""; // Current 'folder' of the package, for example "Asia/"

    public void onPopupBackClick() {
        if (currentFolder.length() == 0) {
            popup.getPopup().getHeader().getBackButton().setVisibility(View.INVISIBLE);
        } else {
            int index = currentFolder.lastIndexOf('/', currentFolder.length() - 2) + 1;
            currentFolder = currentFolder.substring(0, index);
            updatePackages();
        }
    }

    public void downloadComplete() {
        updatePackages();
    }

    public void onStatusChange(final String id, final PackageStatus status) {
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                packageContent.findAndUpdate(id, status);
            }
        });
    }

    public void updatePackages() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final ArrayList<Package> packages = getPackages();

                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        packageContent.addPackages(packages);
                    }
                });
            }
        });

        thread.start();
    }

    public void onPackageClick(final Package item) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (item.isGroup()) {
                    currentFolder = currentFolder + item.name + "/";
                    final ArrayList<Package> packages = getPackages();

                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            packageContent.addPackages(packages);
                            popup.getPopup().getHeader().getBackButton().setVisibility(VISIBLE);
                        }
                    });
                } else {

                    String action = item.getActionText();

                    if (action.equals(Package.ACTION_DOWNLOAD)) {
                        manager.startPackageDownload(item.id);
                    } else if (action.equals(Package.ACTION_PAUSE)) {
                        manager.setPackagePriority(item.id, -1);
                    } else if (action.equals(Package.ACTION_RESUME)) {
                        manager.setPackagePriority(item.id, 0);
                    } else if (action.equals(Package.ACTION_CANCEL)) {
                        manager.cancelPackageTasks(item.id);
                    } else if (action.equals(Package.ACTION_REMOVE)) {
                        manager.startPackageRemove(item.id);
                    }
                }
            }
        });

        thread.start();
    }

    Package getCustomRegionFolder() {
        Package item = new Package();
        item.name = Package.CUSTOM_REGION_FOLDER_NAME;
        item.id = "NONE";
        return item;
    }

    ArrayList<Package> getCustomRegionPackages() {
        ArrayList<Package> packages = new ArrayList<>();

        for (City city : Cities.LIST) {
            Package item = new Package();
            item.id = city.bbox.toString();
            item.name = city.name;
            item.status = manager.getLocalPackageStatus(item.id, -1);
            packages.add(item);
        }

        return packages;
    }
}
