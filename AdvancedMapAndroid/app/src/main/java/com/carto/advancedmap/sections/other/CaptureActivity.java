package com.carto.advancedmap.sections.other;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.R;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.VectorLayer;
import com.carto.renderers.RendererCaptureListener;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.Marker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@ActivityData(name = "Screencapture", description = "Capture rendered mapView as a Bitmap")
public class CaptureActivity extends MapBaseActivity {

	RenderListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

		// MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

		// Add default base layer
		addBaseLayer(BaseMapsView.DEFAULT_STYLE);

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
	}

	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					onPermissionGranted();
				} else {
					finish();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	private MapPos pos = new MapPos();
	private int num = 0;

	public void onPermissionGranted() {

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
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(CaptureActivity.this, "Screen saved to " + filename, Toast.LENGTH_LONG).show();
						share(filename);
					}
				});
			}
		}
	}

	void share(String path) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/png");

		File file = new File(path);
		Uri uri = Uri.fromFile(file);

		intent.putExtra(Intent.EXTRA_STREAM, uri);
		CaptureActivity.this.startActivity(Intent.createChooser(intent, "Share image"));
	}

	@Override
	public void onResume() {
		super.onResume();

		listener = new RenderListener();
		mapView.getMapRenderer().captureRendering(listener, true);
	}

	@Override
	public void onPause() {

		mapView.getMapRenderer().setMapRendererListener(null);
		listener = null;

		super.onPause();
	}

	com.carto.graphics.Bitmap bitmap;

	public class RenderListener extends RendererCaptureListener {

		@Override
		public void onMapRendered(com.carto.graphics.Bitmap bitmap) {
			CaptureActivity.this.bitmap = bitmap;

			if (CaptureActivity.this.isMarshmallow()) {
				requestPermissions(new String[] {
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.READ_EXTERNAL_STORAGE
				});
			} else {
				onPermissionGranted();
			}
		}

	}
}
