package com.carto.advancedmap.utils;

import android.graphics.Color;
import android.os.AsyncTask;

import com.carto.advancedmap.R;
import com.carto.advancedmap.baseclasses.activities.BaseActivity;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.baseclasses.activities.PackageManagerBaseActivity;
import com.carto.advancedmap.sections.routing.RouteMapEventListener;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.MapRange;
import com.carto.core.Variant;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Bitmap;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.routing.RoutingAction;
import com.carto.routing.RoutingInstruction;
import com.carto.routing.RoutingInstructionVector;
import com.carto.routing.RoutingRequest;
import com.carto.routing.RoutingResult;
import com.carto.routing.RoutingService;
import com.carto.styles.BalloonPopupMargins;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.styles.StringCartoCSSStyleSetMap;
import com.carto.ui.MapView;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Marker;

import java.nio.charset.Charset;

import static com.carto.routing.RoutingAction.ROUTING_ACTION_TURN_LEFT;
import static com.carto.routing.RoutingAction.ROUTING_ACTION_TURN_RIGHT;

/**
 * Created by aareundo on 11/04/2017.
 */

public class RouteCalculator {

    private Marker startMarker;
    private Marker stopMarker;
    private MarkerStyle instructionUp;
    private MarkerStyle instructionLeft;
    private MarkerStyle instructionRight;

    private LocalVectorDataSource routeDataSource;
    private LocalVectorDataSource routeStartStopDataSource;

    private VectorLayer routeLayer;

    BaseActivity context;
    MapView mapView;
    RoutingService service;

    public LocalVectorDataSource getRouteSource() {
        return routeDataSource;
    }

    public VectorLayer getRouteLayer() {
        return routeLayer;
    }

    public RouteCalculator(BaseActivity context, MapView mapView, RoutingService service) {

        this.context = context;
        this.mapView = mapView;
        this.service = service;

        Projection baseProjection = mapView.getOptions().getBaseProjection();

        routeDataSource = new LocalVectorDataSource(baseProjection);
        routeLayer = new VectorLayer(routeDataSource);
        mapView.getLayers().add(routeLayer);

        // Define layer and datasource for route start and stop markers
        routeStartStopDataSource = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(routeStartStopDataSource);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer);

        // Set visible zoom range for the vector layer
        vectorLayer.setVisibleZoomRange(new MapRange(0, 22));

        // Set route listener
        RouteMapEventListener mapListener = new RouteMapEventListener(this);
        mapView.setMapEventListener(mapListener);

        // Create markers for start & end, and a layer for them
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setBitmap(createBitmap(R.drawable.olmarker));
        markerStyleBuilder.setHideIfOverlapped(false);
        markerStyleBuilder.setSize(30);

        markerStyleBuilder.setColor(new com.carto.graphics.Color(Color.GREEN));

        startMarker = new Marker(new MapPos(0, 0), markerStyleBuilder.buildStyle());
        startMarker.setVisible(false);

        markerStyleBuilder.setColor(new com.carto.graphics.Color(Color.RED));

        stopMarker = new Marker(new MapPos(0, 0), markerStyleBuilder.buildStyle());
        stopMarker.setVisible(false);

        routeStartStopDataSource.add(startMarker);
        routeStartStopDataSource.add(stopMarker);

        markerStyleBuilder.setColor(new com.carto.graphics.Color(Color.WHITE));
        markerStyleBuilder.setBitmap(createBitmap(R.drawable.direction_up));
        instructionUp = markerStyleBuilder.buildStyle();

        markerStyleBuilder.setBitmap(createBitmap(R.drawable.direction_upthenleft));
        instructionLeft = markerStyleBuilder.buildStyle();

        markerStyleBuilder.setBitmap(createBitmap(R.drawable.direction_upthenright));

        instructionRight = markerStyleBuilder.buildStyle();
    }

    public void showRoute(final MapPos startPos, final MapPos stopPos) {

        AsyncTask<Void, Void, RoutingResult> task = new AsyncTask<Void, Void, RoutingResult>() {

            protected RoutingResult doInBackground(Void... v) {

                RoutingResult result = getResult(startPos, stopPos);

                return result;
            }

            protected void onPostExecute(RoutingResult result) {

                if (result == null) {
                    alert("Routing failed");
                    return;
                }

                float distance = (int)(result.getTotalDistance() / 100) / 10f;
                String time = secondsToHours((int) result.getTotalTime());
                String text = "Your route is " + distance + "km (" + time + ")";

                alert(text);

                routeDataSource.clear();

                startMarker.setVisible(false);

                routeDataSource.add(createPolyline(result));

                // Add instruction markers
                RoutingInstructionVector instructions = result.getInstructions();
                boolean first = true;

                for (int i = 0; i < instructions.size(); i++) {

                    RoutingInstruction instruction = instructions.get(i);

                    if (first) {
                        // Set car to first instruction position
                        first = false;
                    } else {
                        MapPos position = result.getPoints().get(instruction.getPointIndex());
                        createRoutePoint(position, instruction, routeDataSource);
                    }

                }
            }
        };

        task.execute();
    }

    void alert(String text) {

        if (MapBaseActivity.class.isAssignableFrom(context.getClass())) {
            ((MapBaseActivity)context).contentView.banner.alert(text);
        } else if(PackageManagerBaseActivity.class.isAssignableFrom(context.getClass())) {
            ((PackageManagerBaseActivity)context).contentView.banner.alert(text);
        }
    }

    public RoutingResult getResult(MapPos start, MapPos stop) {
        MapPosVector poses = new MapPosVector();

        poses.add(start);
        poses.add(stop);

        RoutingRequest request = new RoutingRequest(mapView.getOptions().getBaseProjection(), poses);

        try {
            return service.calculateRoute(request);
        } catch (Exception e) {
            // Can return IOException and RuntimeException
            return null;
        }
    }

    public static final String DESCRIPTION = "";

    protected void createRoutePoint(MapPos pos, RoutingInstruction instruction, LocalVectorDataSource ds) {

        MarkerStyle style = instructionUp;
        RoutingAction action = instruction.getAction();

        if (action == ROUTING_ACTION_TURN_LEFT) {
            style = instructionLeft;
        } else if (action == ROUTING_ACTION_TURN_RIGHT) {
            style = instructionRight;
        }

        Marker marker = new Marker(pos, style);
        marker.setMetaDataElement(DESCRIPTION, new Variant(instruction.toString()));

        ds.add(marker);
    }

    protected Line createPolyline(RoutingResult result) {

        LineStyleBuilder lineStyleBuilder = new LineStyleBuilder();
        lineStyleBuilder.setColor(new com.carto.graphics.Color(Color.DKGRAY));
        lineStyleBuilder.setWidth(12);

        return new Line(result.getPoints(), lineStyleBuilder.buildStyle());
    }

    protected String secondsToHours(int sec) {
        int hours = sec / 3600;
        int remainder = sec % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        return ( (hours < 10 ? "0" : "") + hours
                + "h" + (minutes < 10 ? "0" : "") + minutes
                + "m" + (seconds< 10 ? "0" : "") + seconds+"s" );
    }

    public void setStartMarker(MapPos startPos) {
        routeDataSource.clear();
        stopMarker.setVisible(false);
        startMarker.setPos(startPos);

        startMarker.setVisible(true);
    }

    public void setStopMarker(MapPos pos) {
        stopMarker.setPos(pos);
        stopMarker.setVisible(true);
    }

    Bitmap createBitmap(int id)
    {
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeResource(context.getResources(), id);
        return BitmapUtils.createBitmapFromAndroidBitmap(bitmap);
    }

}
