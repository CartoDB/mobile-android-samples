package com.carto.advancedmap.sections.basemap;

import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.Feature;
import com.carto.geometry.Geometry;
import com.carto.geometry.LineGeometry;
import com.carto.geometry.MultiGeometry;
import com.carto.geometry.PointGeometry;
import com.carto.geometry.PolygonGeometry;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.layers.VectorTileEventListener;
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

/**
 * Created by aareundo on 08/11/16.
 */

public class VectorTileListener extends VectorTileEventListener {

    VectorLayer layer;

    public VectorTileListener(VectorLayer layer) {
        this.layer = layer;
    }

    @Override
    public boolean onVectorTileClicked(VectorTileClickInfo clickInfo)
    {
        LocalVectorDataSource source = (LocalVectorDataSource)layer.getDataSource();

        source.clear();

        Color color = new Color((short)0, (short)100, (short)200, (short)150);

        Feature feature = clickInfo.getFeature();
        Geometry geometry = feature.getGeometry();

        PointStyleBuilder pointBuilder = new PointStyleBuilder();
        pointBuilder.setColor(color);

        LineStyleBuilder lineBuilder = new LineStyleBuilder();
        lineBuilder.setColor(color);

        PolygonStyleBuilder polygonBuilder = new PolygonStyleBuilder();
        polygonBuilder.setColor(color);

        if (geometry instanceof PointGeometry) {
            source.add(new Point((PointGeometry)geometry, pointBuilder.buildStyle()));
        } else if (geometry instanceof LineGeometry) {
            source.add(new Line((LineGeometry)geometry, lineBuilder.buildStyle()));
        }  else if (geometry instanceof PolygonGeometry) {
            source.add(new Polygon((PolygonGeometry)geometry, polygonBuilder.buildStyle()));
        } else if (geometry instanceof MultiGeometry) {

            GeometryCollectionStyleBuilder collectionBuilder = new GeometryCollectionStyleBuilder();
            collectionBuilder.setPointStyle(pointBuilder.buildStyle());
            collectionBuilder.setLineStyle(lineBuilder.buildStyle());
            collectionBuilder.setPolygonStyle(polygonBuilder.buildStyle());

            source.add(new GeometryCollection((MultiGeometry)geometry, collectionBuilder.buildStyle()));
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

