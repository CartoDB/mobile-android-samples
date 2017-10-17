package com.carto.advancedmap.sections.other;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.carto.advancedmap.sections.basemap.BaseMapsView;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.utils.Colors;
import com.carto.advancedmap.utils.Utils;
import com.carto.core.MapPos;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyle;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.utils.Log;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.Polygon;
import com.carto.core.MapPosVector;

public class GPSLocationActivity extends MapBaseActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    LocalVectorDataSource vectorDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        // Add default base layer
        contentView.addBaseLayer(BaseMapsView.DEFAULT_STYLE);

        // Initialize an local vector data source where to put my location circle
        vectorDataSource = new LocalVectorDataSource(contentView.projection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(vectorDataSource);

        // Add the previous vector layer to the map
        contentView.mapView.getLayers().add(vectorLayer);

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
            }
        }
    }

    void onPermissionGranted() {

        Color lightAppleBlue = Colors.toCartoColor(Colors.LIGHT_TRANSPARENT_APPLE_BLUE);
        Color darkAppleBlue = Colors.toCartoColor(Colors.DARK_TRANSPARENT_APPLE_BLUE);

        // Style for GPS My Location circle
        PolygonStyleBuilder polygonBuilder = new PolygonStyleBuilder();
        polygonBuilder.setColor(lightAppleBlue);
        final Polygon accuracyMarker = new Polygon(new MapPosVector(), polygonBuilder.buildStyle());

        PointStyleBuilder pointBuilder = new PointStyleBuilder();
        pointBuilder.setColor(darkAppleBlue);
        final Point userMarker = new Point(new MapPos(), pointBuilder.buildStyle());

        // Initially empty and invisible
        accuracyMarker.setVisible(false);
        userMarker.setVisible(false);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                accuracyMarker.setPoses(Utils.createLocationCircle(location, contentView.projection));
                accuracyMarker.setVisible(true);

                MapPos position = new MapPos(location.getLongitude(), location.getLatitude());
                position = contentView.projection.fromWgs84(position);

                userMarker.setPos(position);
                userMarker.setVisible(true);

                contentView.mapView.setFocusPos(position, 0.0f);
                contentView.mapView.setZoom(15, 0.0f);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) { }
            @Override
            public void onProviderEnabled(String s) { }
            @Override
            public void onProviderDisabled(String s) { }
        };

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // User has maybe disabled location services / GPS
        if (locationManager.getProviders(true).size() == 0) {
            alert("Cannot get location, no location providers enabled. Check device settings");
        }

        // Use all enabled device providers with same parameters
        for (String provider : locationManager.getProviders(true)) {

            int fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.requestLocationUpdates(provider, 1000, 50, locationListener);
        }

        vectorDataSource.add(accuracyMarker);
        vectorDataSource.add(userMarker);
    }
    
    @Override
    public void onDestroy() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    	super.onDestroy();
    }
}
