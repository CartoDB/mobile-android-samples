package com.carto.cartomap.android.sections.sqlapi;

import android.os.Bundle;

import com.carto.cartomap.android.util.Description;
import com.carto.cartomap.android.basemap.VectorMapSampleBaseActivity;
import com.carto.cartomap.android.datasource.CartoDBSQLDataSource;
import com.carto.cartomap.android.listener.MyMapEventListener;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.styles.PointStyle;
import com.carto.styles.PointStyleBuilder;

/**
 * A sample demonstrating how to use Carto SQL API to get data and how to create custom VectorDataSource
 */
@Description(value = "CARTO SQL API to get data in custom VectorDataSource")
public class CartoSQLActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapSampleBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        String baseUrl = "https://nutiteq.cartodb.com/api/v2/sql";
        String query = "SELECT cartodb_id,the_geom_webmercator AS the_geom,name,address,bikes,slot," +
                "field_8,field_9,field_16,field_17,field_18 FROM stations_1 WHERE !bbox!";

        // Define style for vector objects.
        // Note that all objects must have same style here, which can be big limitation
        PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new Color(0x800000ff)); // blue
        pointStyleBuilder.setSize(10);

        PointStyle style = pointStyleBuilder.buildStyle();

        // Initialize a local vector data source
        CartoDBSQLDataSource vectorDataSource1 = new CartoDBSQLDataSource(baseProjection, baseUrl, query, style);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new VectorLayer(vectorDataSource1);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);

        // Set visible zoom range for the vector layer
        vectorLayer1.setVisibleZoomRange(new MapRange(14, 23));

        // Initialize a local vector data source and layer for click Balloons
        LocalVectorDataSource vectorDataSource = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(vectorDataSource);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer);

        // Set listener to get point click popups
        mapView.setMapEventListener(new MyMapEventListener(mapView, vectorDataSource));

        // Animate map to the marker
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(-74.0059, 40.7127)), 1);
        mapView.setZoom(15, 1);
    }

    @Override
    protected void onDestroy() {
        mapView.setMapEventListener(null);

        super.onDestroy();
    }
}
