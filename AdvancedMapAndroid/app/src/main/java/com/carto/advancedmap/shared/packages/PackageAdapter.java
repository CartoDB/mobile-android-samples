package com.carto.advancedmap.shared.packages;

/**
 * Created by aareundo on 16/12/16.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.carto.advancedmap.sections.offlinemap.advancedpackagemanager.AdvancedPackageManagerActivity;
import com.carto.advancedmap.sections.routing.offline.OfflineRoutingActivity;
import com.carto.packagemanager.PackageAction;
import com.carto.packagemanager.PackageManager;

import java.util.ArrayList;

/**
 * Adapter for displaying packages as a list.
 */
public class PackageAdapter extends ArrayAdapter<Package> {

    Context context;
    int layoutResourceId;
    ArrayList<Package> packages;

    PackageManager packageManager;

    ListView list;

    public PackageAdapter(Context context, int layoutResourceId, ArrayList<Package> packages, PackageManager packageManager) {

        super(context, layoutResourceId, packages);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.packages = packages;
        this.packageManager = packageManager;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        PackageHolder holder;

        // Create new holder object or reuse existing
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PackageHolder();
            holder.nameView = (TextView) row.findViewById(com.carto.advancedmap.R.id.package_name);
            holder.statusView = (TextView) row.findViewById(com.carto.advancedmap.R.id.package_status);
            holder.actionButton = (Button) row.findViewById(com.carto.advancedmap.R.id.package_action);

            row.setTag(holder);
        } else {
            holder = (PackageHolder) row.getTag();
        }

        // Report package name and size
        final Package pkg = packages.get(position);
        holder.nameView.setText(pkg.packageName);

        if (pkg.packageInfo != null) {
            String status = "available";
            if (pkg.packageInfo.getSize().longValue() < 1024 * 1024) {
                status += " v." + pkg.packageInfo.getVersion()+" (<1MB)";
            } else {
                status += " v." + pkg.packageInfo.getVersion()+" (" + pkg.packageInfo.getSize().longValue() / 1024 / 1024 + "MB)";
            }

            // Check if the package is downloaded/is being downloaded (so that status is not null)
            if (pkg.packageStatus != null) {
                if (pkg.packageStatus.getCurrentAction() == PackageAction.PACKAGE_ACTION_READY) {
                    status = "ready";
                    holder.actionButton.setText("RM");
                    holder.actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.startPackageRemove(pkg.packageInfo.getPackageId());
                        }
                    });
                } else if (pkg.packageStatus.getCurrentAction() == PackageAction.PACKAGE_ACTION_WAITING) {
                    status = "queued";
                    holder.actionButton.setText("C");
                    holder.actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.cancelPackageTasks(pkg.packageInfo.getPackageId());
                        }
                    });
                } else {

                    if (pkg.packageStatus.getCurrentAction() == PackageAction.PACKAGE_ACTION_COPYING) {
                        status = "copying";
                    } else if (pkg.packageStatus.getCurrentAction() == PackageAction.PACKAGE_ACTION_DOWNLOADING) {
                        status = "downloading";
                    } else if (pkg.packageStatus.getCurrentAction() == PackageAction.PACKAGE_ACTION_REMOVING) {
                        status = "removing";
                    }

                    status += " " + Integer.toString((int) pkg.packageStatus.getProgress()) + "%";

                    if (pkg.packageStatus.isPaused()) {
                        status = status + " (paused)";
                        holder.actionButton.setText("R");
                        holder.actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                packageManager.setPackagePriority(pkg.packageInfo.getPackageId(), 0);
                            }
                        });
                    } else {
                        holder.actionButton.setText("P");
                        holder.actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                packageManager.setPackagePriority(pkg.packageInfo.getPackageId(), -1);
                            }
                        });
                    }
                }
            } else {
                holder.actionButton.setText("DL");
                holder.actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packageManager.startPackageDownload(pkg.packageInfo.getPackageId());
                    }
                });
            }
            holder.statusView.setText(status);
        } else {
            holder.actionButton.setText(">");
            holder.actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCurrentFolder(pkg);
                    updatePackages();
                }
            });
            holder.statusView.setText("");
        }

        return row;
    }

    public void updateCurrentFolder(Package pkg) {

        if (context instanceof AdvancedPackageManagerActivity) {
            AdvancedPackageManagerActivity activity = (AdvancedPackageManagerActivity)context;
            activity.currentFolder = activity.currentFolder + pkg.packageName + "/";
        } else if (context instanceof OfflineRoutingActivity) {
            // Offline routing features no such foldering system.
        }
    }

    public void updatePackages() {

        if (context instanceof AdvancedPackageManagerActivity) {
            AdvancedPackageManagerActivity activity = (AdvancedPackageManagerActivity)context;
            activity.updatePackages();
        } else if (context instanceof OfflineRoutingActivity) {
            OfflineRoutingActivity activity = (OfflineRoutingActivity)context;
            activity.updatePackages();
        }
    }
}

