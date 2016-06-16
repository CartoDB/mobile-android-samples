package com.carto.advancedmap.listener;

import android.util.Log;

import com.carto.advancedmap.Const;
import com.carto.core.MapPos;
import com.carto.core.StringMap;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.UTFGridEventListener;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.ClickType;
import com.carto.ui.MapView;
import com.carto.ui.UTFGridClickInfo;
import com.carto.vectorelements.BalloonPopup;

import java.util.Locale;

/**
 * A custom map event listener that displays information about map events and creates pop-ups.
 */
public class MyUTFGridEventListener extends UTFGridEventListener {
	private MapView mapView;
	private LocalVectorDataSource vectorDataSource;
	
	private BalloonPopup oldClickLabel;

	public MyUTFGridEventListener(MapView mapView, LocalVectorDataSource vectorDataSource) {
		this.mapView = mapView;
		this.vectorDataSource = vectorDataSource;
	}

	@Override
	public boolean onUTFGridClicked(UTFGridClickInfo clickInfo) {
		Log.d(Const.LOG_TAG, "UTF grid click!");
		
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


		String msg = "";
		StringMap utfData = clickInfo.getElementInfo();
		for (int i=0; i<utfData.size();i++){
			msg += utfData.get_key(i)+ ": "+utfData.get(utfData.get_key(i))+ "\n";
		}

		// finally show click coordinates also
		MapPos wgs84Clickpos = mapView.getOptions().getBaseProjection().toWgs84(clickPos);
		msg  += String.format(Locale.US, "%.4f, %.4f", wgs84Clickpos.getY(), wgs84Clickpos.getX());



		BalloonPopup clickPopup = new BalloonPopup(clickInfo.getClickPos(),
												   styleBuilder.buildStyle(),
		                						   clickMsg,
		                						   msg);
		vectorDataSource.add(clickPopup);
		oldClickLabel = clickPopup;
		
		return true;
	}
}
