package com.carto.advancedmap;

import android.os.Bundle;

import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.core.Variant;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.vectorelements.Marker;

/**
 * Created by aareundo on 09/08/16. TestActivity to test Documentation samples.
 */
public class TestActivity extends MapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * #region Base
         */

        Projection proj = mapView.getOptions().getBaseProjection();

        // Initialize an local vector data source
        LocalVectorDataSource vectorDataSource1 = new LocalVectorDataSource(proj);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new VectorLayer(vectorDataSource1);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);

        // Set limited visible zoom range for the vector layer
        vectorLayer1.setVisibleZoomRange(new MapRange(10, 24));

        /**
         * #endregion Base
         */

        // Create marker style
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setSize(30);
        // Green colour as ARGB
        markerStyleBuilder.setColor(new Color(0xFF00FF00));

        MarkerStyle sharedMarkerStyle = markerStyleBuilder.buildStyle();

        // Add marker
        MapPos tallinn = proj.fromWgs84(new MapPos(24.646469, 59.426939));
        Marker marker1 = new Marker(tallinn, sharedMarkerStyle);
        marker1.setMetaDataElement("ClickText", new Variant("Marker nr 1"));
        vectorDataSource1.add(marker1);

        // Animate map to the marker
        mapView.setFocusPos(tallinn, 1);
        mapView.setZoom(12, 1);
    }
}
