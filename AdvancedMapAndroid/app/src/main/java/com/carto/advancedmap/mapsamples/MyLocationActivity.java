package com.carto.advancedmap.mapsamples;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.core.MapPos;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Color;
import com.carto.advancedmap.util.CircleUtil;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.styles.PolygonStyle;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.utils.Log;
import com.carto.vectorelements.Polygon;
import com.carto.core.MapPosVector;

/**
 * Shows user GPS location on map.
 * Make sure your app has location permission in Manifest file
 */
@Description(value = "Shows user GPS location on the map")
public class MyLocationActivity extends VectorMapSampleBaseActivity {
	
	private LocationManager locationManager;
	private LocationListener locationListener;

    Projection proj;
    LocalVectorDataSource vectorDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapSampleBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        proj = super.baseProjection;

        // Initialize an local vector data source where to put my location circle
        vectorDataSource = new LocalVectorDataSource(proj);
        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(vectorDataSource);
        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer);

        if (isMarshmallow()) {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            onPermissionGranted();
        }
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

    void onPermissionGranted() {

        // style for GPS My Location circle
        PolygonStyleBuilder polygonStyleBuilder = new PolygonStyleBuilder();
        polygonStyleBuilder.setColor(new Color(0xAAFF0000));
        PolygonStyle gpsStyle = polygonStyleBuilder.buildStyle();

        MapPosVector gpsCirclePoses = new MapPosVector();
        final Polygon locationCircle = new Polygon(gpsCirclePoses, gpsStyle);
        // initially empty and invisible
        locationCircle.setVisible(false);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.debug("GPS onLocationChanged " + location);
                if (locationCircle != null) {
                    locationCircle.setPoses(CircleUtil.createLocationCircle(location, proj));
                    locationCircle.setVisible(true);
                    mapView.setFocusPos(proj.fromWgs84(new MapPos(location.getLongitude(), location.getLatitude())), 0.5f);
                    mapView.setZoom(14, 1.0f); // zoom 2, duration 0 seconds (no animation)
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // user has maybe disabled location services / GPS
        if (locationManager.getProviders(true).size() == 0) {
            Toast.makeText(this, "Cannot get location, no location providers enabled. Check device settings", Toast.LENGTH_LONG).show();
        }

        // use all enabled device providers with same parameters
        for (String provider : locationManager.getProviders(true)) {
            Log.debug("adding location provider " + provider);
            locationManager.requestLocationUpdates(provider, 1000, 50, locationListener);
        }

        vectorDataSource.add(locationCircle);

    }
    
    @Override
    public void onDestroy() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    	super.onDestroy();
    }
}
