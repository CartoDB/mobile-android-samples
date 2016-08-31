package com.carto.cartomap.android.map;

import android.os.Bundle;

import com.carto.cartomap.android.Description;
import com.carto.cartomap.android.map_base.VectorMapSampleBaseActivity;
import com.carto.cartomap.android.map_datasource.CartoDBSQLDataSource;
import com.carto.cartomap.android.map_listener.MyMapEventListener;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.styles.PointStyleBuilder;

/**
 * A sample demonstrating how to use Carto SQL API to get data and how to create custom VectorDataSource
 */
@Description(value = "How to use Carto SQL API to get data and how to create custom VectorDataSource")
public class CartoSQLActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapSampleBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        // define style for vector objects. Note that all objects must have same style here, which can be big limitation
        PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new Color(0x800000ff)); // blue
        pointStyleBuilder.setSize(10);

        String query = "SELECT cartodb_id,the_geom_webmercator AS the_geom,name,address,bikes,slot," +
                "field_8,field_9,field_16,field_17,field_18 FROM stations_1 WHERE !bbox!";

        // Initialize a local vector data source
        CartoDBSQLDataSource vectorDataSource1 = new CartoDBSQLDataSource(
                baseProjection,
                "https://nutiteq.cartodb.com/api/v2/sql",
                query,
                pointStyleBuilder.buildStyle()
        );

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new VectorLayer(vectorDataSource1);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);

        // Set visible zoom range for the vector layer
        vectorLayer1.setVisibleZoomRange(new MapRange(14, 23));


        // set listener to get point click popups

        // Initialize a local vector data source and layer for click Balloons
        LocalVectorDataSource vectorDataSource = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(vectorDataSource);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer);

        mapView.setMapEventListener(new MyMapEventListener(mapView, vectorDataSource));


        // finally animate map to the marker
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(-74.0059, 40.7127)), 1);
        mapView.setZoom(15, 1);
    }

    @Override
    protected void onDestroy() {
        mapView.setMapEventListener(null);

        super.onDestroy();
    }
}
