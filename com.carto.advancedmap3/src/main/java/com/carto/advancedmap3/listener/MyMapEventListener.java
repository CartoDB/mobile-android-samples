package com.carto.advancedmap3.listener;

import java.util.Locale;

import android.util.Log;

import com.carto.advancedmap3.Const;
import com.carto.core.MapPos;
import com.carto.core.ScreenPos;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.UTFGridRasterTileLayer;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.ClickType;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.MapView;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Billboard;
import com.carto.vectorelements.VectorElement;
import com.carto.core.StringMap;

/**
 * A custom map event listener that displays information about map events and creates pop-ups.
 */
public class MyMapEventListener extends MapEventListener {
	private MapView mapView;
	private LocalVectorDataSource vectorDataSource;
	
	private BalloonPopup oldClickLabel;
	private UTFGridRasterTileLayer gridLayer;

	public MyMapEventListener(MapView mapView, LocalVectorDataSource vectorDataSource) {
		this.mapView = mapView;
		this.vectorDataSource = vectorDataSource;
	}

	public void setGridLayer(UTFGridRasterTileLayer gridLayer) {
		this.gridLayer = gridLayer;
	}

	@Override
	public void onMapMoved() {

        final MapPos topLeft = mapView.screenToMap(new ScreenPos(0, 0));
        final MapPos bottomRight = mapView.screenToMap(new ScreenPos(mapView.getWidth(), mapView.getHeight()));


		MapPos mapPos = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(0, 0));
		ScreenPos screenPos = mapView.mapToScreen(mapPos);

		Log.d(Const.LOG_TAG, mapView.getOptions().getBaseProjection().toWgs84(topLeft)
                + " " + mapView.getOptions().getBaseProjection().toWgs84(bottomRight));

		Log.d(Const.LOG_TAG, "screen for 0,0 : " + screenPos.getX()+ " "+screenPos.getY());


	}

	@Override
	public void onMapClicked(MapClickInfo mapClickInfo) {
		Log.d(Const.LOG_TAG, "Map click!");
		
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


		String msg = "";

		// if UtfGridLayer is set, then try to show all metadata from it
		if(this.gridLayer != null){

			StringMap utfData = this.gridLayer.getTooltips(clickPos,true);
			for (int i=0; i<utfData.size();i++){
				msg += utfData.get_key(i)+ ": "+utfData.get(utfData.get_key(i))+ "\n";
			}

		}

		// finally show click coordinates also
		MapPos wgs84Clickpos = mapView.getOptions().getBaseProjection().toWgs84(clickPos);
		msg  += String.format(Locale.US, "%.4f, %.4f", wgs84Clickpos.getY(), wgs84Clickpos.getX());



		BalloonPopup clickPopup = new BalloonPopup(mapClickInfo.getClickPos(),
												   styleBuilder.buildStyle(),
		                						   clickMsg,
		                						   msg);
		vectorDataSource.add(clickPopup);
		oldClickLabel = clickPopup;
	}

}
