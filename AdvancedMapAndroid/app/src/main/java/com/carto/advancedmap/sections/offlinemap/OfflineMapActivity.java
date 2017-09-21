package com.carto.advancedmap.sections.offlinemap;

import android.os.Bundle;

import com.carto.advancedmap.baseclasses.activities.PackageManagerBaseActivity;
import com.carto.advancedmap.baseclasses.views.PackageManagerBaseView;
import com.carto.advancedmap.utils.Sources;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOfflineVectorTileLayer;
import com.carto.packagemanager.CartoPackageManager;

public class OfflineMapActivity extends PackageManagerBaseActivity {

	@Override
	public String getFolderName() {
		return "com.carto.mappackages";
	}

	@Override
	public String getSource() {
		return Sources.CARTO_VECTOR;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		contentView = new PackageManagerBaseView(this);
		setContentView(contentView);

		super.onCreate(savedInstanceState);

		addLayer();
	}

	void addLayer() {

		CartoPackageManager manager = contentView.manager;
		CartoBaseMapStyle style = CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER;
		CartoOfflineVectorTileLayer layer = new CartoOfflineVectorTileLayer(manager, style);
		contentView.mapView.getLayers().add(layer);
	}
}
