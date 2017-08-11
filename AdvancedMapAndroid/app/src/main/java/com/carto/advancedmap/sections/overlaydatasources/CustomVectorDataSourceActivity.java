package com.carto.advancedmap.sections.overlaydatasources;

import android.os.Bundle;

import com.carto.advancedmap.shared.activities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.core.MapBounds;
import com.carto.core.MapPos;
import com.carto.datasources.VectorDataSource;
import com.carto.datasources.components.VectorData;
import com.carto.graphics.Color;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.renderers.components.CullState;
import com.carto.styles.PointStyle;
import com.carto.styles.PointStyleBuilder;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.VectorElementVector;

@ActivityData(name = "Custom Vector Data Source", description = "Customized vector data source")
public class CustomVectorDataSourceActivity extends MapBaseActivity {

    public static final int NUM_POINTS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add default base layer
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_POSITRON);

        // Create custom vector data source
        VectorDataSource customDataSource = new MyVectorDataSource(mapView.getOptions().getBaseProjection(), NUM_POINTS);

        // Create overlay vector layer
        VectorLayer overlayLayer = new VectorLayer(customDataSource);
        mapView.getLayers().add(overlayLayer);

        // finally animate map to a nice place
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(0, 0)), 0);
        mapView.setZoom(3, 1);
    }

    /**
     * A custom vector data source that generates random points and selects only visible points
     * for each view.
     */
    private static class MyVectorDataSource extends VectorDataSource {
        private Point[] points;

        public MyVectorDataSource(Projection proj, int numPoints) {
            super(proj);

            // Generate random points with default style
            points = new Point[numPoints];
            PointStyleBuilder styleBuilder = new PointStyleBuilder();
            styleBuilder.setColor(new Color(0xff0080ff));
            styleBuilder.setSize(10.0f);
            PointStyle style = styleBuilder.buildStyle();
            MapBounds bounds = proj.getBounds();
            for (int i = 0; i < numPoints; i++) {
                double x = Math.random() * bounds.getDelta().getX() + bounds.getMin().getX();
                double y = Math.random() * bounds.getDelta().getY() + bounds.getMin().getY();
                points[i] = new Point(new MapPos(x, y), style);
            }
        }

        @Override
        public VectorData loadElements(CullState cullState) {
            MapBounds viewBounds = cullState.getProjectionEnvelope(getProjection()).getBounds();

            // Filter elements based on position, keep only visible elements
            VectorElementVector elements = new VectorElementVector();
            for (Point point : points) {
                if (viewBounds.contains(point.getPos())) {
                    elements.add(point);
                }
            }
            return new VectorData(elements);
        }
    }
}
