package com.carto.cartomap.android.maplistener;

import android.util.Log;

import com.carto.core.MapPos;
import com.carto.core.ScreenPos;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.ClickType;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.MapView;
import com.carto.vectorelements.BalloonPopup;

import java.util.Locale;

/**
 * A custom map event listener that displays information about map events and creates pop-ups.
 */
public class MyMapEventListener extends MapEventListener {

	private MapView mapView;
	private LocalVectorDataSource vectorDataSource;
	
	private BalloonPopup oldClickLabel;

	public MyMapEventListener(MapView mapView, LocalVectorDataSource vectorDataSource) {
		this.mapView = mapView;
		this.vectorDataSource = vectorDataSource;
	}

	@Override
	public void onMapMoved() {

        final MapPos topLeft = mapView.screenToMap(new ScreenPos(0, 0));
        final MapPos bottomRight = mapView.screenToMap(new ScreenPos(mapView.getWidth(), mapView.getHeight()));

		MapPos mapPos = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(0, 0));
		ScreenPos screenPos = mapView.mapToScreen(mapPos);

		Log.d("LOG", mapView.getOptions().getBaseProjection().toWgs84(topLeft)
                + " " + mapView.getOptions().getBaseProjection().toWgs84(bottomRight));

		Log.d("LOG", "screen for 0,0 : " + screenPos.getX()+ " "+screenPos.getY());
	}

	@Override
	public void onMapClicked(MapClickInfo mapClickInfo) {
		Log.d("LOG", "Map click!");
		
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
		if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_SINGLE) {
			clickMsg = "Single map click!";
		} else if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
			clickMsg = "Long map click!";
		} else if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_DOUBLE) {
			clickMsg = "Double map click!";
		} else if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_DUAL) {
			clickMsg ="Dual map click!";
		}

		MapPos clickPos = mapClickInfo.getClickPos();

		// finally show click coordinates also
		MapPos wgs84Clickpos = mapView.getOptions().getBaseProjection().toWgs84(clickPos);

		String msg = String.format(Locale.US, "%.4f, %.4f", wgs84Clickpos.getY(), wgs84Clickpos.getX());

		BalloonPopup clickPopup = new BalloonPopup(mapClickInfo.getClickPos(),
												   styleBuilder.buildStyle(),
		                						   clickMsg,
		                						   msg);
		vectorDataSource.add(clickPopup);
		oldClickLabel = clickPopup;
	}
}
