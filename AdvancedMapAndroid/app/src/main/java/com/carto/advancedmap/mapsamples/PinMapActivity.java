package com.carto.advancedmap.mapsamples;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.R;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.VectorLayer;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.Marker;

/**
 * A sample demonstrating how to use markers on the map. This involves creating
 * a data source for the markers, creating a layer using the data source, loading
 * marker bitmaps, creating style for the marker and finally adding the marker to the data source.
 * For multiple markers, the same data source, layer and style should be reused if possible.
 */
@Description(value = "Base map with Marker pins")
public class PinMapActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapSampleBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        // Add a pin marker to map
        // 1. Initialize a local vector data source
        LocalVectorDataSource vectorDataSource1 = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new VectorLayer(vectorDataSource1);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);

        // Set visible zoom range for the vector layer
        vectorLayer1.setVisibleZoomRange(new MapRange(0, 18));
        
        // 2. Create marker style
        Bitmap androidMarkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        com.carto.graphics.Bitmap markerBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidMarkerBitmap);
        
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setBitmap(markerBitmap);
        //markerStyleBuilder.setHideIfOverlapped(false);
        markerStyleBuilder.setSize(30);
        MarkerStyle sharedMarkerStyle = markerStyleBuilder.buildStyle();
        
        // 3. Add marker
        MapPos markerPos = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(13.38933, 52.51704)); // Berlin
        Marker marker1 = new Marker(markerPos, sharedMarkerStyle);
        vectorDataSource1.add(marker1);
        
        // finally animate map to the marker
        mapView.setFocusPos(markerPos, 1);
        mapView.setZoom(12, 1);
    }
}
