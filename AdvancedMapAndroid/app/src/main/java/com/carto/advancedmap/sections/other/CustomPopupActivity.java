package com.carto.advancedmap.sections.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.carto.advancedmap.util.Description;
import com.carto.advancedmap.R;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.advancedmap.vectorelements.MyCustomPopupHandler;
import com.carto.core.MapPos;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.VectorLayer;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.styles.PopupStyle;
import com.carto.styles.PopupStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.CustomPopup;
import com.carto.vectorelements.Marker;

/**
 * A sample demonstrating how to create and use custom popups.
 * Note that Carto Mobile SDK has built-in customizable BalloonPopup class
 * that provides uniform functionality and look across different platforms.
 * But In some cases more customization is needed and Popup subclassing can be used.
 */
@Description(value = "Create custom popups")
public class CustomPopupActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);

        // Initialize a local vector data source
        LocalVectorDataSource vectorDataSource1 = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new VectorLayer(vectorDataSource1);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);
        
        // Create marker style
        Bitmap androidMarkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        com.carto.graphics.Bitmap markerBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidMarkerBitmap);
        
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setBitmap(markerBitmap);
        markerStyleBuilder.setSize(30);
        MarkerStyle markerStyle = markerStyleBuilder.buildStyle();
        
        // Add marker
        MapPos markerPos = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(13.38933, 52.51704)); // Berlin
        Marker marker1 = new Marker(markerPos, markerStyle);
        vectorDataSource1.add(marker1);
        
        // Add popup
    	PopupStyleBuilder popupStyleBuilder = new PopupStyleBuilder();
    	popupStyleBuilder.setAttachAnchorPoint(0.5f, 0);
    	PopupStyle popupStyle = popupStyleBuilder.buildStyle();

    	MyCustomPopupHandler popupHandler = new MyCustomPopupHandler("custom popup");

        CustomPopup popup1 = new CustomPopup(marker1, popupStyle, popupHandler);
        popup1.setAnchorPoint(-1, 0);
        vectorDataSource1.add(popup1);
        
        // Animate map to the marker
        mapView.setFocusPos(markerPos, 1);
        mapView.setZoom(12, 1);
    }
}
