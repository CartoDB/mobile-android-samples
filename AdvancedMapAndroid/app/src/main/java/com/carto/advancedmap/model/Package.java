package com.carto.advancedmap.model;

/**
 * Created by aareundo on 16/12/16.
 */


import com.carto.packagemanager.PackageAction;
import com.carto.packagemanager.PackageInfo;
import com.carto.packagemanager.PackageStatus;

import java.util.Comparator;

/**
 * A full package info, containing package info and status.
 */
public class Package {

    public String name;
    public String id;
    public PackageInfo info;
    public PackageStatus status;

    public Package(String name, PackageInfo packageInfo, PackageStatus status) {
        this.name = name;
        this.id = (packageInfo != null ? packageInfo.getPackageId() : null);
        this.info = packageInfo;
        this.status = status;
    }

    public static class PackageComparator implements Comparator<Package> {
        @Override
        public int compare(Package s, Package t) {
            return s.name.compareTo(t.name);
        }
    }

    public boolean isCustomRegionFolder() {
        return name.equals(Package.CUSTOM_REGION_FOLDER_NAME);
    }

    public boolean isCustomRegionPackage() {
        if (id == null) {
            return false;
        }

        return id.contains(Package.BBOX_IDENTIFIER);
    }

    public boolean isGroup() {
        // Custom region packages will have status & info == null, but they're not groups
        return status == null && info == null && !isCustomRegionPackage();
    }

    boolean isSmallerThan1MB() {
        return info.getSize().longValue() < (long)(1024 * 1024);
    }

    public String getStatusText() {

        String status = "Available";

        status += getVersionAndSize();

        if (this.status != null) {

            PackageAction action = this.status.getCurrentAction();

            if (action == PackageAction.PACKAGE_ACTION_READY) {
                status = "Ready";
            } else if (action == PackageAction.PACKAGE_ACTION_WAITING) {
                status = "Queued";
            } else {
                if (action == PackageAction.PACKAGE_ACTION_COPYING) {
                    status = "Copying";
                } else if (action == PackageAction.PACKAGE_ACTION_DOWNLOADING) {
                    status = "Downloading";
                } else if (action == PackageAction.PACKAGE_ACTION_REMOVING) {
                    status = "Removing";
                }

                status += " " + this.status.getProgress() + "%";
            }
        }

        return status;
    }

    String getSizeInMb() {
        return Double.toString(info.getSize().longValue()  / (1024 * 1024));
    }

    String getVersionAndSize() {

        if (isCustomRegionPackage()) {
            return  "";
        }

        String version = Integer.toString(info.getVersion());
        String size = getSizeInMb();

        if (isSmallerThan1MB()) {
            return " v." + version + " (<1MB)";
        }

        return " v." + version + " (" + size + " MB)";
    }

    public boolean isDownloading() {

        if (status == null) {
            return false;
        }

        return status.getCurrentAction() == PackageAction.PACKAGE_ACTION_DOWNLOADING && !status.isPaused();
    }

    public boolean isQueued() {
        if (status == null) {
            return false;
        }

        return status.getCurrentAction() == PackageAction.PACKAGE_ACTION_WAITING && !status.isPaused();
    }

    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_RESUME = "RESUME";
    public static final String ACTION_CANCEL = "CANCEL";
    public static final String ACTION_DOWNLOAD = "DOWNLOAD";
    public static final String ACTION_REMOVE = "REMOVE";

    public static final String CUSTOM_REGION_FOLDER_NAME = "CUSTOM_REGION";
    public static final String BBOX_IDENTIFIER = "bbox(";

    public String getActionText() {

        if (status == null) {
            return ACTION_DOWNLOAD;
        }

        PackageAction action = status.getCurrentAction();

        if (action == PackageAction.PACKAGE_ACTION_READY) {
            return ACTION_REMOVE;
        } else if (action == PackageAction.PACKAGE_ACTION_WAITING) {
            return ACTION_CANCEL;
        }

        if (status.isPaused()) {
            return ACTION_RESUME;
        } else {
            return ACTION_PAUSE;
        }
    }
}

