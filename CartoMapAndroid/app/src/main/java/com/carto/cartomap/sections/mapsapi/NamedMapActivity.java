package com.carto.cartomap.sections.mapsapi;

import android.os.Bundle;

import com.carto.cartomap.sections.BaseMapActivity;
import com.carto.cartomap.util.ActivityData;
import com.carto.core.MapPos;
import com.carto.core.StringVariantMap;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.Feature;
import com.carto.geometry.Geometry;
import com.carto.geometry.LineGeometry;
import com.carto.geometry.MultiGeometry;
import com.carto.geometry.PointGeometry;
import com.carto.geometry.PolygonGeometry;
import com.carto.graphics.Color;
import com.carto.layers.Layer;
import com.carto.layers.LayerVector;
import com.carto.layers.TileLayer;
import com.carto.layers.VectorLayer;
import com.carto.layers.VectorTileEventListener;
import com.carto.layers.VectorTileLayer;
import com.carto.projections.Projection;
import com.carto.services.CartoMapsService;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.GeometryCollectionStyleBuilder;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.ui.VectorTileClickInfo;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.GeometryCollection;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.Polygon;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

@ActivityData(name = "Named Map", description = "CARTO data as Vector Tiles from a named map")
public class NamedMapActivity extends BaseMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // BaseMapActivity creates and sets mapView
        super.onCreate(savedInstanceState);

        final CartoMapsService service = new CartoMapsService();

        // Use raster layers, not vector
        service.setDefaultVectorLayerMode(true);

        service.setUsername("nutiteq");

        final String name = "tpl_69f3eebe_33b6_11e6_8634_0e5db1731f59";

        // Be sure to make network queries on another thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    LayerVector layers = service.buildNamedMap(name, new StringVariantMap());

                    for (int i = 0; i < layers.size(); i++) {
                        Layer layer = layers.get(i);
                        mapView.getLayers().add(layer);

                        if (layer instanceof VectorTileLayer) {
                            initializeVectorTileListener((VectorTileLayer)layer);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        MapPos position = baseProjection.fromLatLong(37.32549682016584, -121.94595158100128);
        mapView.setFocusPos(position, 0);
        mapView.setZoom(19, 1);
    }

    VectorTileListener initializeVectorTileListener(VectorTileLayer tileLayer) {

        Projection projection = mapView.getOptions().getBaseProjection();
        LocalVectorDataSource source = new LocalVectorDataSource(projection);

        VectorLayer vectorLayer = findTileLayer();

        if (vectorLayer == null) {
            vectorLayer = new VectorLayer(source);
            mapView.getLayers().add(vectorLayer);
        }

        VectorTileListener listener = new VectorTileListener(vectorLayer);

        tileLayer.setVectorTileEventListener(listener);

        return listener;
    }

    public VectorLayer findTileLayer()
    {
        for (int i = 0; i < mapView.getLayers().count(); i++)
        {
            Layer layer = mapView.getLayers().get(i);

            if (layer instanceof VectorLayer)
            {
                return (VectorLayer)layer;
            }

        }

        return null;
    }

    private class VectorTileListener extends VectorTileEventListener {

        VectorLayer layer;

        public VectorTileListener(VectorLayer layer) {
            this.layer = layer;
        }

        @Override
        public boolean onVectorTileClicked(VectorTileClickInfo clickInfo) {
            LocalVectorDataSource source = (LocalVectorDataSource) layer.getDataSource();

            source.clear();

            Color color = new Color((short) 0, (short) 100, (short) 200, (short) 150);

            Feature feature = clickInfo.getFeature();
            Geometry geometry = feature.getGeometry();

            PointStyleBuilder pointBuilder = new PointStyleBuilder();
            pointBuilder.setColor(color);

            LineStyleBuilder lineBuilder = new LineStyleBuilder();
            lineBuilder.setColor(color);

            PolygonStyleBuilder polygonBuilder = new PolygonStyleBuilder();
            polygonBuilder.setColor(color);

            if (geometry instanceof PointGeometry) {
                source.add(new Point((PointGeometry) geometry, pointBuilder.buildStyle()));
            } else if (geometry instanceof LineGeometry) {
                source.add(new Line((LineGeometry) geometry, lineBuilder.buildStyle()));
            } else if (geometry instanceof PolygonGeometry) {
                source.add(new Polygon((PolygonGeometry) geometry, polygonBuilder.buildStyle()));
            } else if (geometry instanceof MultiGeometry) {

                GeometryCollectionStyleBuilder collectionBuilder = new GeometryCollectionStyleBuilder();
                collectionBuilder.setPointStyle(pointBuilder.buildStyle());
                collectionBuilder.setLineStyle(lineBuilder.buildStyle());
                collectionBuilder.setPolygonStyle(polygonBuilder.buildStyle());

                source.add(new GeometryCollection((MultiGeometry) geometry, collectionBuilder.buildStyle()));
            }

            BalloonPopupStyleBuilder builder = new BalloonPopupStyleBuilder();

            // Set a higher placement priority so it would always be visible
            builder.setPlacementPriority(10);

            String message = feature.getProperties().toString();

            BalloonPopup popup = new BalloonPopup(clickInfo.getClickPos(), builder.buildStyle(), "Click", message);

            source.add(popup);

            return true;
        }
    }
}
