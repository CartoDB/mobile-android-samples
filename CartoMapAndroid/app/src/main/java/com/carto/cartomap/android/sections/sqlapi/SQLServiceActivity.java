package com.carto.cartomap.android.sections.sqlapi;

import android.os.Bundle;

import com.carto.cartomap.android._old.datasource.CartoDBSQLDataSource;
import com.carto.cartomap.android.sections.BaseMapActivity;
import com.carto.cartomap.android.util.Description;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.datasources.VectorDataSource;
import com.carto.datasources.components.VectorData;
import com.carto.geometry.FeatureCollection;
import com.carto.geometry.LineGeometry;
import com.carto.geometry.PointGeometry;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.renderers.components.CullState;
import com.carto.services.CartoSQLService;
import com.carto.styles.PointStyle;
import com.carto.styles.PointStyleBuilder;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.VectorElementVector;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

@Description(value = "Displays cities on the map via SQL query")
public class SQLServiceActivity extends BaseMapActivity {

    static final String query = "SELECT * FROM cities15000 WHERE population > 100000";
//    static final String query = "SELECT * FROM cities15000";

    FeatureCollection features = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LocalVectorDataSource source = new LocalVectorDataSource(baseProjection);
        final VectorLayer layer = new VectorLayer(source);
        mapView.getLayers().add(layer);

        final CartoSQLService service = new CartoSQLService();
        service.setUsername("nutiteq");

        // Be sure to make network queries on another thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    features = service.queryFeatures(query);

                    for (int i = 0; i < features.getFeatureCount(); i++) {

                        // This data set features point geometry,
                        // however, it can also be LineGeometry or PolygonGeometry

                        PointGeometry geometry = (PointGeometry)features.getFeature(i).getGeometry();
                        source.add(new Point(geometry, getPointStyle()));
                        System.out.println("Element: " + geometry);
                    }

                    System.out.println("Total: " + features.getFeatureCount());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    PointStyle getPointStyle() {

        PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new Color(android.graphics.Color.CYAN));
        pointStyleBuilder.setSize(10);

        return pointStyleBuilder.buildStyle();
    }
}
