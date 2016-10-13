package com.carto.cartomap.android.listener;

import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.UTFGridEventListener;
import com.carto.layers.VectorLayer;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.UTFGridClickInfo;
import com.carto.vectorelements.BalloonPopup;

/**
 * A custom map event listener that displays information about map events and creates pop-ups.
 */

public class MyUTFGridEventListener extends UTFGridEventListener {
	private VectorLayer vectorLayer;

	public MyUTFGridEventListener(VectorLayer vectorLayer) {
		this.vectorLayer = vectorLayer;
	}

	@Override
	public boolean onUTFGridClicked(UTFGridClickInfo utfGridClickInfo) {
		LocalVectorDataSource vectorDataSource = (LocalVectorDataSource) vectorLayer.getDataSource();

		// Clear previous popups
		vectorDataSource.clear();

		BalloonPopupStyleBuilder styleBuilder = new BalloonPopupStyleBuilder();

		// Configure style
		styleBuilder.setLeftMargins(new BalloonPopupMargins(0, 0, 0, 0));
		styleBuilder.setTitleMargins(new BalloonPopupMargins(6, 3, 6, 3));

		// Make sure this label is shown on top all other labels
		styleBuilder.setPlacementPriority(10);

		// Show clicked element variant as JSON string
		String desc = utfGridClickInfo.getElementInfo().toString();

		BalloonPopup clickPopup = new BalloonPopup(utfGridClickInfo.getClickPos(), styleBuilder.buildStyle(), "Clicked", desc);
		vectorDataSource.add(clickPopup);

		return true;
	}
}

