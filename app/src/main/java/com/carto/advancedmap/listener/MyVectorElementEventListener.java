package com.carto.advancedmap.listener;

import android.util.Log;

import com.carto.advancedmap.Const;
import com.carto.layers.VectorElementEventListener;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.ui.MapView;
import com.carto.ui.VectorElementClickInfo;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Billboard;
import com.carto.vectorelements.VectorElement;
import com.carto.core.StringMap;

public class MyVectorElementEventListener extends VectorElementEventListener {
	protected MapView mapView;
	protected LocalVectorDataSource vectorDataSource;
	
	private BalloonPopup oldClickLabel;

	public MyVectorElementEventListener(MapView mapView, LocalVectorDataSource vectorDataSource) {
		this.mapView = mapView;
		this.vectorDataSource = vectorDataSource;
	}

	@Override
	public boolean onVectorElementClicked(VectorElementClickInfo clickInfo) {
		Log.d(Const.LOG_TAG, "Vector element click!");
		
		// Remove old click label
		if (oldClickLabel != null) {
			vectorDataSource.remove(oldClickLabel);
			oldClickLabel = null;
		}
		
		// Multiple vector elements can be clicked at the same time, we only care about the one
		// Check the type of vector element
		BalloonPopup clickPopup = null;
		BalloonPopupStyleBuilder styleBuilder = new BalloonPopupStyleBuilder();
	    // Configure style
	    styleBuilder.setLeftMargins(new BalloonPopupMargins(0, 0, 0, 0));
	    styleBuilder.setTitleMargins(new BalloonPopupMargins(6, 3, 6, 3));
	    // Make sure this label is shown on top all other labels
	    styleBuilder.setPlacementPriority(10);

		VectorElement vectorElement = clickInfo.getVectorElement();
		String clickText = vectorElement.getMetaDataElement("ClickText");

		// show all metadata elements
		StringMap stringMap = vectorElement.getMetaData();
		StringBuilder msgBuilder = new StringBuilder();
		if (stringMap.size() > 0) {
			for (int i = 0; i < stringMap.size(); i++) {
				Log.d(Const.LOG_TAG, "" + stringMap.get_key(i) + " = " + stringMap.get(stringMap.get_key(i)));
				if(!stringMap.get_key(i).equals("ClickText")){
					msgBuilder.append(stringMap.get_key(i));
					msgBuilder.append("=");
					msgBuilder.append(stringMap.get(stringMap.get_key(i)));
					msgBuilder.append("\n");
				}
			}
		}
		String desc = msgBuilder.toString().trim();

		if (vectorElement instanceof Billboard) {
			// If the element is billboard, attach the click label to the billboard element
			Billboard billboard = (Billboard) vectorElement;
			clickPopup = new BalloonPopup(billboard, 
										  styleBuilder.buildStyle(),
		                    			  clickText, 
		                    			  desc);
		} else {
			// for lines and polygons set label to click location
			clickPopup = new BalloonPopup(clickInfo.getElementClickPos(),
										  styleBuilder.buildStyle(),
		                   				  clickText,
					desc);
		}
		vectorDataSource.add(clickPopup);
		oldClickLabel = clickPopup;
		return true;
	}

	

}
