package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.content.Intent;
import com.carto.advancedmap.main.ActivityData;
import com.carto.advancedmap.utils.Sources;

public class OfflineMapActivity extends PackageManagerBaseActivity {

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
