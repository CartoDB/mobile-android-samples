package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.carto.advancedmap.shared.Colors;
import com.carto.advancedmap.MapApplication;
import com.carto.advancedmap.shared.packages.PackageAdapter;
import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.R;
import com.carto.core.StringVector;

import com.carto.packagemanager.CartoPackageManager;
import com.carto.packagemanager.PackageErrorType;
import com.carto.packagemanager.PackageInfo;
import com.carto.packagemanager.PackageInfoVector;
import com.carto.packagemanager.PackageManagerListener;
import com.carto.packagemanager.PackageStatus;
import com.carto.advancedmap.shared.packages.Package;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@ActivityData(name = "Advanced Package Manager", description = "Download offline map packages with OSM")
public class AdvancedPackageManagerActivity extends ListActivity {

	/**
	 * Listener for package manager events.
	 */
	class PackageListener extends PackageManagerListener {
		@Override
		public void onPackageListUpdated() {
			updatePackages();
		}

		@Override
		public void onPackageListFailed() {
			updatePackages();
			displayToast("Failed to download package list");
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
			displayToast("Failed to download package " + id + "/" + version + ": " + errorType);
		}
	}

	private CartoPackageManager packageManager;
	private ArrayAdapter<Package> packageAdapter;
	private ArrayList<Package> packageArray = new ArrayList<Package>();
	
	public String currentFolder = ""; // Current 'folder' of the package, for example "Asia/"
	private String language = "en"; // Language for the package names. Most major languages are supported

	public static CartoPackageManager Manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ColorDrawable background = new ColorDrawable(Colors.ACTION_BAR);
		getActionBar().setBackgroundDrawable(background);

		// Create package manager
        File packageFolder = new File(getApplicationContext().getExternalFilesDir(null), "regionpackages");
        if (!(packageFolder.mkdirs() || packageFolder.isDirectory())) {
        	Log.e(MapApplication.LOG_TAG, "Could not create package folder!");
        }
		try {
			packageManager = new CartoPackageManager("nutiteq.osm", packageFolder.getAbsolutePath());
		}
		catch (IOException e) {
			Log.e(MapApplication.LOG_TAG, "Exception: " + e);
			finish();
		}

		packageManager.setPackageManagerListener(new PackageListener());
		packageManager.startPackageListDownload();

        // Initialize list view
        setContentView(R.layout.list);
        packageAdapter = new PackageAdapter(this, com.carto.advancedmap.R.layout.package_item_row, packageArray, packageManager);
        getListView().setAdapter(packageAdapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		String title = getIntent().getStringExtra("title");
		String description = getIntent().getStringExtra("description");

		if (title != null) {
			setTitle(title);
		}

		if (description != null) {
			getActionBar().setSubtitle(description);
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
	public void onStart() {
		super.onStart();
		packageManager.start();
	}
	
	@Override
	public void onStop() {
		packageManager.stop(false);
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		packageManager.setPackageManagerListener(null);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		if (currentFolder.length() == 0) {
			super.onBackPressed();
		} else {
			currentFolder = currentFolder.substring(0, currentFolder.lastIndexOf('/', currentFolder.length() - 2) + 1);
			updatePackages();
		}
	}
	
	private ArrayList<Package> getPackages() {
		ArrayList<Package> pkgs = new ArrayList<>();

		PackageInfoVector packageInfoVector = packageManager.getServerPackages();
		for (int i = 0; i < packageInfoVector.size(); i++) {
			PackageInfo packageInfo = packageInfoVector.get(i);

			// Get the list of names for this package. Each package may have multiple names,
			// packages are grouped using '/' as a separator, so the the full name for Sweden
			// is "Europe/Northern Europe/Sweden". We display packages as a tree, so we need
			// to extract only relevant packages belonging to the current folder.
			StringVector packageNames = packageInfo.getNames(language);

			for (int j = 0; j < packageNames.size(); j++) {
				String packageName = packageNames.get(j);

				if (!packageName.startsWith(currentFolder)) {
					continue; // belongs to a different folder, so ignore
				}

				packageName = packageName.substring(currentFolder.length());
				int index = packageName.indexOf('/');
				Package pkg;

				if (index == -1) {
					// This is actual package
					PackageStatus packageStatus = packageManager.getLocalPackageStatus(packageInfo.getPackageId(), -1);
					pkg = new Package(packageName, packageInfo, packageStatus);
				} else {
					// This is package group
					packageName = packageName.substring(0, index);

					// Try n' find an existing package from the list.
					ArrayList<Package> existingPackages = new ArrayList<>();

					for (Package existingPackage : pkgs) {
						if (existingPackage.packageName.equals(packageName)) {
							existingPackages.add(existingPackage);
						}
					}

					if (existingPackages.size() == 0) {
						// If there are none, add a package group if we don't have an existing list item
						pkg = new Package(packageName, null, null);
					} else if (existingPackages.size() == 1 && existingPackages.get(0).packageInfo != null) {

						// Sometimes we need to add two labels with the same name.
						// One a downloadable package and the other pointing to a list of said country's counties,
						// such as with Spain, Germany, France, Great Britain

						// If there is one existing package and its info isn't null,
						// we will add a "parent" package containing subpackages (or package group)
						pkg = new Package(packageName, null, null);

					} else {
						// Shouldn't be added, as both cases are accounted for
						continue;
					}
				}
				pkgs.add(pkg);
			}
		}
		return pkgs;
	}
	
	public void updatePackages() {
		if (packageAdapter == null) {
			return;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Simply reload all packages from the scratch
				packageArray.clear();
				packageArray.addAll(getPackages());
				packageAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void updatePackage(final String packageId) {
		if (packageAdapter == null) {
			return;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Try to find the package that needs to be updated
				for (int i = 0; i < packageArray.size(); i++) {
					Package pkg = packageArray.get(i);
					if (packageId.equals(pkg.packageId)) {
						PackageStatus packageStatus = packageManager.getLocalPackageStatus(packageId, -1);
						pkg = new Package(pkg.packageName, pkg.packageInfo, packageStatus);
						packageArray.set(i, pkg);
						// TODO: it would be much better to only refresh the changed row
						packageAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
	
	private void displayToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@SuppressLint({ "InlinedApi", "NewApi" })
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    
	    MenuItem menuItem = menu.add("Map").setOnMenuItemClickListener(new OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick (MenuItem item){
               
                // Using static global variable to pass data. Avoid this in your app (memory leaks etc)!
                Manager = AdvancedPackageManagerActivity.this.packageManager;
                
                Intent myIntent = new Intent(AdvancedPackageManagerActivity.this, PackagedMapActivity.class);
                AdvancedPackageManagerActivity.this.startActivity(myIntent);
                
                return true;
            }
        });
	    
	    menuItem.setIcon(android.R.drawable.ic_dialog_map);
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    }
	    return super.onCreateOptionsMenu(menu);
	}
}
