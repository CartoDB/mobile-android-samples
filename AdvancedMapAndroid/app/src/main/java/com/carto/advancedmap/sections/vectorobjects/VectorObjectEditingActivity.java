package com.carto.advancedmap.sections.vectorobjects;

import android.os.Bundle;

import com.carto.advancedmap.shared.activities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.Geometry;
import com.carto.geometry.LineGeometry;
import com.carto.geometry.PointGeometry;
import com.carto.geometry.PolygonGeometry;
import com.carto.graphics.Color;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.EditableVectorLayer;
import com.carto.layers.VectorEditEventListener;
import com.carto.layers.VectorElementDragPointStyle;
import com.carto.layers.VectorElementDragResult;
import com.carto.layers.VectorElementEventListener;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.PointStyle;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.VectorElementClickInfo;
import com.carto.ui.VectorElementDragInfo;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.Polygon;
import com.carto.vectorelements.VectorElement;

@ActivityData(name = "Vector Object Editing", description = "Shows usage of an editable vector layer")
public class VectorObjectEditingActivity extends MapBaseActivity {

    LocalVectorDataSource source;

    EditableVectorLayer editLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add default base layer
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY);

        source = new LocalVectorDataSource(mapView.getOptions().getBaseProjection());

        editLayer = new EditableVectorLayer(source);
        mapView.getLayers().add(editLayer);

        addPoint(new MapPos(-5000000, -900000));

        MapPosVector linePositions = new MapPosVector();
        linePositions.add(new MapPos(-6000000, -500000));
        linePositions.add(new MapPos(-9000000, -500000));

        addLine(linePositions);

        MapPosVector polygonPositions = new MapPosVector();
        polygonPositions.add(new MapPos(-5000000, -5000000));
        polygonPositions.add(new MapPos(5000000, -5000000));
        polygonPositions.add(new MapPos(0, 10000000));

        addPolygon(polygonPositions);

        // Add a vector element event listener to select elements (on element click)
        editLayer.setVectorElementEventListener(new VectorElementSelectEventListener(editLayer));

        // Add a map event listener to deselect element (on map click)
        mapView.setMapEventListener(new VectorElementDeselectEventListener(editLayer));

        // Add the vector element edit event listener
        editLayer.setVectorEditEventListener(new BasicEditEventListener(source));
    }

    @Override
    public void onDestroy() {

        // Add a vector element event listener to select elements (on element click)
        editLayer.setVectorElementEventListener(null);

        // Add a map event listener to deselect element (on map click)
        mapView.setMapEventListener(null);

        // Add the vector element edit event listener
        editLayer.setVectorEditEventListener(null);

        super.onDestroy();
    }

    private void addPoint(MapPos position)
    {
        PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new Color(android.graphics.Color.BLUE));

        Point point = new Point(position, pointStyleBuilder.buildStyle());

        source.add(point);
    }

    private void addLine(MapPosVector positions)
    {
        LineStyleBuilder lineStyleBuilder = new LineStyleBuilder();
        lineStyleBuilder.setColor(new Color(android.graphics.Color.RED));

        Line line = new Line(positions, lineStyleBuilder.buildStyle());

        source.add(line);
    }

    private void addPolygon(MapPosVector positions)
    {
        PolygonStyleBuilder polygonStyleBuilder = new PolygonStyleBuilder();
        polygonStyleBuilder.setColor(new Color(android.graphics.Color.GREEN));

        Polygon polygon = new Polygon(positions, polygonStyleBuilder.buildStyle());

        source.add(polygon);
    }

    /**********************
        Element select listener
     **********************/

    private class VectorElementSelectEventListener extends VectorElementEventListener
    {
        EditableVectorLayer vectorLayer;

        VectorElementSelectEventListener(EditableVectorLayer vectorLayer)
        {
            this.vectorLayer = vectorLayer;
        }

        @Override
        public boolean onVectorElementClicked(VectorElementClickInfo clickInfo)
        {
            vectorLayer.setSelectedVectorElement(clickInfo.getVectorElement());
            return true;
        }
    }

    /**********************
        Element deselect listener
     **********************/

    public class VectorElementDeselectEventListener extends MapEventListener
    {
        EditableVectorLayer vectorLayer;

        VectorElementDeselectEventListener(EditableVectorLayer vectorLayer)
        {
            this.vectorLayer = vectorLayer;
        }

        @Override
        public void onMapClicked(MapClickInfo mapClickInfo)
        {
            vectorLayer.setSelectedVectorElement(null);
        }
    }

    /**********************
        Edit event listener
     **********************/

    private class BasicEditEventListener extends VectorEditEventListener
    {
        PointStyle styleNormal, styleVirtual, styleSelected;

        LocalVectorDataSource source;

        BasicEditEventListener(LocalVectorDataSource source)
        {
            this.source = source;
        }

        @Override
        public void onElementModify(VectorElement element, Geometry geometry)
        {
            if (element instanceof Point && geometry instanceof PointGeometry) {
                ((Point) element).setGeometry((PointGeometry) geometry);
            }
            if (element instanceof Line && geometry instanceof LineGeometry) {
                ((Line) element).setGeometry((LineGeometry) geometry);
            }
            if (element instanceof Polygon && geometry instanceof PolygonGeometry) {
                ((Polygon) element).setGeometry((PolygonGeometry) geometry);
            }
        }

        @Override
        public void onElementDelete(VectorElement element)
        {
            source.remove(element);
        }

        @Override
        public VectorElementDragResult onDragStart(VectorElementDragInfo dragInfo)
        {
            return VectorElementDragResult.VECTOR_ELEMENT_DRAG_RESULT_MODIFY;
        }
        @Override

        public VectorElementDragResult onDragMove(VectorElementDragInfo dragInfo)
        {
            return VectorElementDragResult.VECTOR_ELEMENT_DRAG_RESULT_MODIFY;
        }

        @Override
        public VectorElementDragResult onDragEnd(VectorElementDragInfo dragInfo)
        {
            return VectorElementDragResult.VECTOR_ELEMENT_DRAG_RESULT_MODIFY;
        }

        @Override
        public PointStyle onSelectDragPointStyle(VectorElement element, VectorElementDragPointStyle dragPointStyle)
        {
            if (styleNormal == null)
            {
                PointStyleBuilder builder = new PointStyleBuilder();
                builder.setColor(new Color(android.graphics.Color.CYAN));
                builder.setSize(20);

                styleNormal = builder.buildStyle();

                builder.setSize(15);

                styleVirtual = builder.buildStyle();

                builder.setColor(new Color(android.graphics.Color.YELLOW));
                builder.setSize(30);

                styleSelected = builder.buildStyle();
            }

            if (dragPointStyle == VectorElementDragPointStyle.VECTOR_ELEMENT_DRAG_POINT_STYLE_SELECTED) {
                return styleSelected;
            }

            if (dragPointStyle == VectorElementDragPointStyle.VECTOR_ELEMENT_DRAG_POINT_STYLE_VIRTUAL) {
                return styleVirtual;
            }

            return styleNormal;

        }
    }
}
