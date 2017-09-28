package com.carto.cartomap.sections.sqlapi;

import android.os.Bundle;

import com.carto.cartomap.sections.MapBaseActivity;
import com.carto.cartomap.util.ActivityData;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.FeatureCollection;
import com.carto.geometry.PointGeometry;
import com.carto.graphics.Color;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.VectorLayer;
import com.carto.services.CartoSQLService;
import com.carto.styles.PointStyle;
import com.carto.styles.PointStyleBuilder;
import com.carto.vectorelements.Point;
import com.carto.vectortiles.MBVectorTileDecoder;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

@ActivityData(name = "SQL Service", description = "Displays cities on the map via SQL query")
public class SQLServiceActivity extends MapBaseActivity {

    static final String query = "SELECT * FROM cities15000 WHERE population > 100000";

    FeatureCollection features = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapBaseActivity creates and sets mapView
        super.onCreate(savedInstanceState);

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARKMATTER);

        // We have only added a single layer
        CartoOnlineVectorTileLayer baselayer = (CartoOnlineVectorTileLayer)mapView.getLayers().get(0);
        // We can get the tile decoder like this from our default base layer
        // and remove texts so dots would be more prominent
        ((MBVectorTileDecoder)baselayer.getTileDecoder()).setStyleParameter("lang", "nolang");

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
                    features = service.queryFeatures(query, baseProjection);

                    for (int i = 0; i < features.getFeatureCount(); i++) {

                        // This data set features point geometry,
                        // however, it can also be LineGeometry or PolygonGeometry

                        PointGeometry geometry = (PointGeometry)features.getFeature(i).getGeometry();
                        source.add(new Point(geometry, getPointStyle()));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    PointStyle getPointStyle() {

        PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new Color(android.graphics.Color.RED));
        pointStyleBuilder.setSize(1);

        return pointStyleBuilder.buildStyle();
    }
}
