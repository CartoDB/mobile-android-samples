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

/*public class MyUTFGridEventListener extends UTFGridEventListener {
	private MapView mapView;
	private LocalVectorDataSource vectorDataSource;
	
	private BalloonPopup oldClickLabel;

	public MyUTFGridEventListener(MapView mapView, LocalVectorDataSource vectorDataSource) {
		this.mapView = mapView;
		this.vectorDataSource = vectorDataSource;
	}

	@Override
	public boolean onUTFGridClicked(UTFGridClickInfo clickInfo) {
		Log.d("LOG", "UTF grid click!");

		System.out.println("onUTFGridClicked");
		// Remove old click label
		if (oldClickLabel != null) {
			vectorDataSource.remove(oldClickLabel);
			oldClickLabel = null;
		}
		
		BalloonPopupStyleBuilder styleBuilder = new BalloonPopupStyleBuilder();
	    // Make sure this label is shown on top all other labels
	    styleBuilder.setPlacementPriority(10);
		
		// Check the type of the click
	    String clickMsg = null;
		if (clickInfo.getClickType() == ClickType.CLICK_TYPE_SINGLE) {
			clickMsg = "Single map click!";
		} else if (clickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
			clickMsg = "Long map click!";
		} else if (clickInfo.getClickType() == ClickType.CLICK_TYPE_DOUBLE) {
			clickMsg = "Double map click!";
		} else if (clickInfo.getClickType() == ClickType.CLICK_TYPE_DUAL) {
			clickMsg ="Dual map click!";
		}

		MapPos clickPos = clickInfo.getClickPos();


		String msg = clickInfo.getElementInfo().toString();

		// Show click coordinates also
		MapPos wgs84Clickpos = mapView.getOptions().getBaseProjection().toWgs84(clickPos);
		msg  += String.format(Locale.US, "%.4f, %.4f", wgs84Clickpos.getY(), wgs84Clickpos.getX());

		BalloonPopup clickPopup = new BalloonPopup(clickInfo.getClickPos(), styleBuilder.buildStyle(), clickMsg, msg);

		vectorDataSource.add(clickPopup);
		oldClickLabel = clickPopup;
		
		return true;
	}
}
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

