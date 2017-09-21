package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.content.Intent;
import android.os.Bundle;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contentView = new PackageManagerBaseView(this);
		setContentView(contentView);
	}
}
