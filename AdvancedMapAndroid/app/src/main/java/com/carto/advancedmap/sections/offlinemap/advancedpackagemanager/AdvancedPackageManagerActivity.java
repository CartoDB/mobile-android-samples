package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.content.Intent;
import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.utils.Sources;

@ActivityData(name = "Advanced Package Manager", description = "Download offline map packages")
public class AdvancedPackageManagerActivity extends PackageManagerBaseActivity {

	@Override
	public String getFolderName() {
		return "regionpackages";
	}

	@Override
	public String getSource() {
		return Sources.CARTO_VECTOR;
	}

	@Override
	public void onMapIconClick() {
		// Using static global variable to pass data. Avoid this in your app (memory leaks etc)!
		MapPackageActivity.manager = this.packageManager;

		Intent myIntent = new Intent(this, MapPackageActivity.class);
		startActivity(myIntent);
	}
}
