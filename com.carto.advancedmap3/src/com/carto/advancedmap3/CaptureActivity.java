package com.carto.advancedmap3;

import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import com.carto.advancedmap3.R;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.Layer;
import com.carto.layers.VectorLayer;
import com.carto.renderers.RendererCaptureListener;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.Marker;
import com.carto.vectorelements.VectorElement;

/**
 * A sample demonstrating how to capture rendered MapView as a bitmap.
 * The sample saves MapView screenshot on the sdcard of the phone.
 * The image is saved only after map has become stable (no tiles are missing).
 */
public class CaptureActivity extends VectorMapSampleBaseActivity {
	
	private class RenderListener extends RendererCaptureListener {
		private MapPos pos = new MapPos();
		private int num = 0;

		@Override
		public void onMapRendered(com.carto.graphics.Bitmap bitmap) {
			if (!mapView.getFocusPos().equals(pos)) {
				pos = mapView.getFocusPos();
				Bitmap bmp = com.carto.utils.BitmapUtils.createAndroidBitmapFromBitmap(bitmap);

				num++;
				String path = Environment.getExternalStorageDirectory().toString();
				String filename = path + "/screen" + num + ".png";

				FileOutputStream out = null;
				try {
				    out = new FileOutputStream(filename);
				    bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
				} catch (Exception e) {
				    e.printStackTrace();
				} finally {
				    try {
				        if (out != null) {
				            out.close();
				        }
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}			
			}
			//mapView.getMapRenderer().captureRendering(this, true);
		}
	}

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
        markerStyleBuilder.setSize(30);
        MarkerStyle sharedMarkerStyle = markerStyleBuilder.buildStyle();
        
        // 3. Add marker
        MapPos markerPos = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(13.38933, 52.51704)); // Berlin
        Marker marker1 = new Marker(markerPos, sharedMarkerStyle);
        vectorDataSource1.add(marker1);
        
        // finally animate map to the marker
        mapView.setFocusPos(markerPos, 1);
        mapView.setZoom(12, 1);
        mapView.getMapRenderer().captureRendering(new RenderListener(), true);
    }
}
