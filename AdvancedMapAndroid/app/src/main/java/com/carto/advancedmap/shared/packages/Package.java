package com.carto.advancedmap.shared.packages;

/**
 * Created by aareundo on 16/12/16.
 */


import com.carto.packagemanager.PackageInfo;
import com.carto.packagemanager.PackageStatus;

import java.util.Comparator;

/**
 * A full package info, containing package info and status.
 */
public class Package {

    public String packageName;
    public String packageId;
    public PackageInfo packageInfo;
    public PackageStatus packageStatus;

    public Package(String packageName, PackageInfo packageInfo, PackageStatus packageStatus) {
        this.packageName = packageName;
        this.packageId = (packageInfo != null ? packageInfo.getPackageId() : null);
        this.packageInfo = packageInfo;
        this.packageStatus = packageStatus;
    }

    public static class PackageComparator implements Comparator<Package> {
        @Override
        public int compare(Package s, Package t) {
            return s.packageName.compareTo(t.packageName);
        }
    }
}

