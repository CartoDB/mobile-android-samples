package com.carto.advancedmap.sections.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.R;
import com.carto.advancedmap.baseactivities.VectorMapSampleBaseActivity;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.VectorLayer;
import com.carto.renderers.RendererCaptureListener;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.Marker;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A sample demonstrating how to capture rendered MapView as a bitmap.
 * The sample saves MapView screenshot on the sdcard of the phone.
 * The image is saved only after map has become stable (no tiles are missing).
 */
@ActivityData(name = "Screencapture", description = "Capture rendered MapView as a Bitmap")
public class CaptureActivity extends VectorMapSampleBaseActivity {

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
        
        // Create marker style
        Bitmap androidMarkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        com.carto.graphics.Bitmap markerBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidMarkerBitmap);
        
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setBitmap(markerBitmap);
        markerStyleBuilder.setSize(30);
        MarkerStyle sharedMarkerStyle = markerStyleBuilder.buildStyle();
        
        // Add marker
        MapPos berlin = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(13.38933, 52.51704));
        Marker marker1 = new Marker(berlin, sharedMarkerStyle);
        vectorDataSource1.add(marker1);
        
        // Animate map to the marker
        mapView.setFocusPos(berlin, 1);
        mapView.setZoom(12, 1);
        mapView.getMapRenderer().captureRendering(new RenderListener(), true);
    }


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
				final String filename = path + "/screen" + num + ".png";

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
					CaptureActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(CaptureActivity.this, "Screen saved to " + filename, Toast.LENGTH_LONG).show();
						}
					});
				}
			}
			//mapView.getMapRenderer().captureRendering(this, true);
		}
	}
}
