package com.carto.advancedmap.map;

import android.os.Bundle;

import com.carto.advancedmap.listener.MyMapEventListener;
import com.carto.advancedmap.listener.MyVectorElementEventListener;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.Layer;
import com.carto.layers.VectorLayer;

/**
 * A sample displaying how to set custom MapListener
 * to detect clicks on map and on map vector elements.
 * Actual vector elements are added in Overlays2DActivity, as this class extends it.
 */
public class MapListenerActivity extends Overlays2DActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        // Overlays2DActivity adds 2D vector elements to map  
        super.onCreate(savedInstanceState);
        
        // 1. Initialize a local vector data source and layer for click Balloons
        LocalVectorDataSource vectorDataSource = new LocalVectorDataSource(baseProjection);
        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(vectorDataSource);
        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer);
        // Set visible zoom range for the vector layer
        vectorLayer.setVisibleZoomRange(new MapRange(10, 24));
        
        // 2. Create and set a map event listener, 
        // it needs the data source for balloons
        MyMapEventListener mapEventListener = new MyMapEventListener(mapView, vectorDataSource);
        mapView.setMapEventListener(mapEventListener);
        
        MyVectorElementEventListener vectorElementEventListener = new MyVectorElementEventListener(mapView, vectorDataSource);
        for (int i = 0; i < mapView.getLayers().count(); i++) {
        	Layer layer = mapView.getLayers().get(i);
        	if (layer instanceof VectorLayer) {
        		((VectorLayer) layer).setVectorElementEventListener(vectorElementEventListener);
        	}
        }
    }
    
    @Override
    public void onDestroy() {
        for (int i = 0; i < mapView.getLayers().count(); i++) {
        	Layer layer = mapView.getLayers().get(i);
        	if (layer instanceof VectorLayer) {
        		((VectorLayer) layer).setVectorElementEventListener(null);
        	}
        }
    	mapView.setMapEventListener(null);
    	super.onDestroy();
    }
}
