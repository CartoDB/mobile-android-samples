package com.carto.advancedmap.mapbase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ZoomControls;

import com.carto.advancedmap.R;
import com.carto.advancedmap.util.Const;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.TileLayer;
import com.carto.projections.EPSG3857;
import com.carto.projections.Projection;
import com.carto.ui.MapView;

/**
 * Base activity for map samples. Includes simple lifecycle management
 */
public class MapSampleBaseActivity extends BaseActivity {

    protected MapView mapView;
    protected Projection baseProjection;
    protected TileLayer baseLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create map view 
        setContentView(R.layout.activity_main);
        mapView = (MapView) this.findViewById(R.id.map_view);
        baseProjection = mapView.getOptions().getBaseProjection();

        // Set default location and some other options
        MapPos berlin = baseProjection.fromWgs84(new MapPos(13.38933, 52.51704));
        mapView.setFocusPos(berlin, 0);
        mapView.setZoom(2, 0);
        mapView.setMapRotation(0, 0);
        mapView.setTilt(90, 0);

        baseLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
        mapView.getLayers().add(baseLayer);
    }
    
    @Override
    public void onPause() {
    	mapView.onPause(); 
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	mapView.onResume();
    }
}
