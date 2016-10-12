package com.carto.advancedmap.mapsamples;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.R;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.MapPosVectorVector;
import com.carto.core.MapRange;
import com.carto.core.Variant;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.BillboardOrientation;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.styles.TextStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Marker;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.Polygon;
import com.carto.vectorelements.Text;

/**
 * A sample demonstrating how to add basic 2D objects to the map: lines, points, polygon with hole, texts and pop-ups.
 */
@Description(value = "2D objects: lines, points, polygon with hole, texts and pop-ups")
public class Overlays2DActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);
        
        Projection proj = super.baseProjection;
        
        // Initialize an local vector data source
        LocalVectorDataSource vectorDataSource1 = new LocalVectorDataSource(proj);
        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new VectorLayer(vectorDataSource1);
        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);
        // Set visible zoom range for the vector layer
        vectorLayer1.setVisibleZoomRange(new MapRange(10, 24));
        
        // Initialize a second vector data source and vector layer
        // This secondary vector layer will be used for drawing borders for
        // line elements (by drawing the same line twice, with different widths)
        // Drawing order withing a layer is currently undefined
        // Using multiple layers is the only way to guarantee
        // that point, line and polygon elements are drawn in a specific order
        LocalVectorDataSource vectorDataSource2 = new LocalVectorDataSource(proj);
        VectorLayer vectorLayer2 = new VectorLayer(vectorDataSource2);
        mapView.getLayers().add(vectorLayer2);
        vectorLayer2.setVisibleZoomRange(new MapRange(10, 24));
        
        // Vector elements
        // Add points
        // First point
        PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new Color(0xFF00FF00));
        pointStyleBuilder.setSize(16);

        Point point1 = new Point(proj.fromWgs84(new MapPos(24.651488, 59.423581)), pointStyleBuilder.buildStyle());
        point1.setMetaDataElement("ClickText", new Variant("Point nr 1"));
        vectorDataSource1.add(point1);

        // Second point
        pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new Color(0xFF0000FF));

        Point point2 = new Point(proj.fromWgs84(new MapPos(24.655994, 59.422716)), pointStyleBuilder.buildStyle());
        point2.setMetaDataElement("ClickText", new Variant("Point nr 2"));
        vectorDataSource1.add(point2);
        
        // Add lines
        // Create line style, and line poses
        LineStyleBuilder lineStyleBuilder = new LineStyleBuilder();
        lineStyleBuilder.setColor(new Color(0xFFFFFFFF));
        lineStyleBuilder.setWidth(8);

        MapPosVector linePoses = new MapPosVector();

        linePoses.add(proj.fromWgs84(new MapPos(24.645565, 59.422074)));
        linePoses.add(proj.fromWgs84(new MapPos(24.643076, 59.420502)));
        linePoses.add(proj.fromWgs84(new MapPos(24.645351, 59.419149)));
        linePoses.add(proj.fromWgs84(new MapPos(24.648956, 59.420393)));
        linePoses.add(proj.fromWgs84(new MapPos(24.650887, 59.422707)));

        // Add first line
        Line line1 = new Line(linePoses, lineStyleBuilder.buildStyle());
        line1.setMetaDataElement("ClickText", new Variant("Line nr 1"));
        vectorDataSource2.add(line1);
        
        // Create another line style, use the same lines poses
        lineStyleBuilder = new LineStyleBuilder();
        lineStyleBuilder.setColor(new Color(0xFFCC0F00));
        lineStyleBuilder.setWidth(12);

        // Add second line to the second layer.
        Line line2 = new Line(linePoses, lineStyleBuilder.buildStyle());
        line2.setMetaDataElement("ClickText", new Variant("Line nr 2"));
        vectorDataSource1.add(line2);
        
        // Create polygon style and poses
        PolygonStyleBuilder polygonStyleBuilder = new PolygonStyleBuilder();
        polygonStyleBuilder.setColor(new Color(0xFFFF0000));
        lineStyleBuilder = new LineStyleBuilder();
        lineStyleBuilder.setColor(new Color(0xFF000000));
        lineStyleBuilder.setWidth(1.0f);
        polygonStyleBuilder.setLineStyle(lineStyleBuilder.buildStyle());

        MapPosVector polygonPoses = new MapPosVector();
        polygonPoses.add(proj.fromWgs84(new MapPos(24.650930, 59.421659)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.657453, 59.416354)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.661187, 59.414607)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.667667, 59.418123)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.665736, 59.421703)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.661444, 59.421245)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.660199, 59.420677)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.656552, 59.420175)));
        polygonPoses.add(proj.fromWgs84(new MapPos(24.654010, 59.421472)));

        // Create 2 polygon holes
        MapPosVector holePoses1 = new MapPosVector();
        holePoses1.add(proj.fromWgs84(new MapPos(24.658409, 59.420522)));
        holePoses1.add(proj.fromWgs84(new MapPos(24.662207, 59.418896)));
        holePoses1.add(proj.fromWgs84(new MapPos(24.662207, 59.417411)));
        holePoses1.add(proj.fromWgs84(new MapPos(24.659524, 59.417171)));
        holePoses1.add(proj.fromWgs84(new MapPos(24.657615, 59.419834)));

        MapPosVector holePoses2 = new MapPosVector();
        holePoses2.add(proj.fromWgs84(new MapPos(24.665640, 59.421243)));
        holePoses2.add(proj.fromWgs84(new MapPos(24.668923, 59.419463)));
        holePoses2.add(proj.fromWgs84(new MapPos(24.662893, 59.419365)));

        MapPosVectorVector polygonHoles = new MapPosVectorVector();
        polygonHoles.add(holePoses1);
        polygonHoles.add(holePoses2);

        // Add polygon
        Polygon polygon = new Polygon(polygonPoses, polygonHoles, polygonStyleBuilder.buildStyle());
        polygon.setMetaDataElement("ClickText", new Variant("Polygon"));
        vectorDataSource1.add(polygon);

        // Create text style
        TextStyleBuilder textStyleBuilder = new TextStyleBuilder();
        textStyleBuilder.setColor(new Color(0xFFFF0000));
        textStyleBuilder.setOrientationMode(BillboardOrientation.BILLBOARD_ORIENTATION_FACE_CAMERA);

        // This enables higher resolution texts for retina devices, but consumes more memory and is slower
        textStyleBuilder.setScaleWithDPI(false);

        // Add text
        Text textpopup1 = new Text(proj.fromWgs84(new MapPos(24.653302, 59.422269)),
                                                      textStyleBuilder.buildStyle(),
                                                      "Face camera text");

        textpopup1.setMetaDataElement("ClickText", new Variant("Text nr 1"));
        vectorDataSource1.add(textpopup1);

        // Add text
        textStyleBuilder = new TextStyleBuilder();
        textStyleBuilder.setOrientationMode(BillboardOrientation.BILLBOARD_ORIENTATION_FACE_CAMERA_GROUND);
        Text textpopup2 = new Text(proj.fromWgs84(new MapPos(24.633216, 59.426869)),
                                                      textStyleBuilder.buildStyle(),
                                                      "Face camera ground text");
        textpopup2.setMetaDataElement("ClickText", new Variant("Text nr 2"));
        vectorDataSource1.add(textpopup2);

        // Add text
        textStyleBuilder = new TextStyleBuilder();
        textStyleBuilder.setFontSize(22);
        textStyleBuilder.setOrientationMode(BillboardOrientation.BILLBOARD_ORIENTATION_GROUND);

        Text textpopup3 = new Text(proj.fromWgs84(new MapPos(24.646457, 59.420839)),
                                                      textStyleBuilder.buildStyle(),
                                                      "Ground text");
        textpopup3.setMetaDataElement("ClickText", new Variant("Text nr 3"));
        vectorDataSource1.add(textpopup3);
        
        // Load bitmaps for custom markers
        Bitmap androidMarkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        com.carto.graphics.Bitmap markerBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidMarkerBitmap);
        
        // Create marker style
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setBitmap(markerBitmap);
        markerStyleBuilder.setSize(30);

        MarkerStyle sharedMarkerStyle = markerStyleBuilder.buildStyle();

        // Add marker
        Marker marker1 = new Marker(proj.fromWgs84(new MapPos(24.646469, 59.426939)), sharedMarkerStyle);
        marker1.setMetaDataElement("ClickText", new Variant("Marker nr 1"));
        vectorDataSource1.add(marker1);

        // Add marker
        Marker marker2 = new Marker(proj.fromWgs84(new MapPos(24.666469, 59.422939)), sharedMarkerStyle);
        marker2.setMetaDataElement("ClickText", new Variant("Marker nr 2"));
        vectorDataSource1.add(marker2);
        
        // Load bitmaps to show on the label
        Bitmap infoImage = BitmapFactory.decodeResource(getResources(), R.drawable.info);
        Bitmap arrowImage = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        
        // Add popup
        BalloonPopupStyleBuilder balloonPopupStyleBuilder = new BalloonPopupStyleBuilder();
        balloonPopupStyleBuilder.setCornerRadius(20);
        balloonPopupStyleBuilder.setLeftMargins(new BalloonPopupMargins(6, 6, 6, 6));
        balloonPopupStyleBuilder.setLeftImage(BitmapUtils.createBitmapFromAndroidBitmap(infoImage));
        balloonPopupStyleBuilder.setRightImage(BitmapUtils.createBitmapFromAndroidBitmap(arrowImage));
        balloonPopupStyleBuilder.setRightMargins(new BalloonPopupMargins(2, 6, 12, 6));
        balloonPopupStyleBuilder.setPlacementPriority(1);
        BalloonPopup popup1 = new BalloonPopup(proj.fromWgs84(new MapPos(24.655662, 59.425521)),
                                               balloonPopupStyleBuilder.buildStyle(),
                                               "Popup with pos",
                                               "Images, round");

        popup1.setMetaDataElement("ClickText", new Variant("Popup nr 1"));
        vectorDataSource1.add(popup1);

        // Add popup, but instead of giving it a position attach it to a marker
        balloonPopupStyleBuilder = new BalloonPopupStyleBuilder();
        balloonPopupStyleBuilder.setColor(new Color(0xFF000000));
        balloonPopupStyleBuilder.setCornerRadius(0);
        balloonPopupStyleBuilder.setTitleColor(new Color(0xFFFFFFFF));
        balloonPopupStyleBuilder.setTitleFontName("HelveticaNeue-Medium");
        balloonPopupStyleBuilder.setDescriptionColor(new Color(0xFFFFFFFF));
        balloonPopupStyleBuilder.setDescriptionFontName("HelveticaNeue-Medium");
        balloonPopupStyleBuilder.setStrokeColor(new Color(0xFF00B483));
        balloonPopupStyleBuilder.setStrokeWidth(0);
        balloonPopupStyleBuilder.setPlacementPriority(1);
        BalloonPopup popup2 = new BalloonPopup(marker1, balloonPopupStyleBuilder.buildStyle(),
                                               "Popup attached to marker", "Black, rectangle.");

        popup2.setMetaDataElement("ClickText", new Variant("Popup nr 2"));
        vectorDataSource1.add(popup2);

        // Add popup
        balloonPopupStyleBuilder = new BalloonPopupStyleBuilder();
        balloonPopupStyleBuilder.setDescriptionWrap(false);
        balloonPopupStyleBuilder.setPlacementPriority(1);

        BalloonPopup popup3 = new BalloonPopup(proj.fromWgs84(new MapPos(24.658662, 59.432521)),
                                               balloonPopupStyleBuilder.buildStyle(),
                                               "This title will be wrapped if there's not enough space on the screen.",
                                               "Description is set to be truncated with three dots, unless the screen is really really big.");

        popup3.setMetaDataElement("ClickText", new Variant("Popup nr 3"));

        vectorDataSource1.add(popup3);

        // Animate map to Tallinn where the objects are
        mapView.setFocusPos(proj.fromWgs84(new MapPos(24.662893, 59.419365)), 1);
        mapView.setZoom(12, 1);
    }
}
