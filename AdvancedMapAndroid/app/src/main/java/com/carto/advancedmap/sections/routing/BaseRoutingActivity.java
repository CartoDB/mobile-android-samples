package com.carto.advancedmap.sections.routing;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.carto.advancedmap.R;
import com.carto.advancedmap.shared.activities.MapBaseActivity;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Bitmap;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.VectorLayer;
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
import com.carto.ui.ClickType;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.MapView;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Marker;

import java.io.IOException;

/**
 * Created by aareundo on 16/12/16.
 */

public class BaseRoutingActivity extends MapBaseActivity {

    private Marker startMarker;
    private Marker stopMarker;
    private MarkerStyle instructionUp;
    private MarkerStyle instructionLeft;
    private MarkerStyle instructionRight;
    private LocalVectorDataSource routeDataSource;
    private LocalVectorDataSource routeStartStopDataSource;
    private BalloonPopupStyleBuilder balloonPopupStyleBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addBaseLayer();

        routeDataSource = new LocalVectorDataSource(baseProjection);
        VectorLayer routeLayer = new VectorLayer(routeDataSource);
        getMapView().getLayers().add(routeLayer);

        // Define layer and datasource for route start and stop markers
        routeStartStopDataSource = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(routeStartStopDataSource);

        // Add the previous vector layer to the map
        getMapView().getLayers().add(vectorLayer);

        // Set visible zoom range for the vector layer
        vectorLayer.setVisibleZoomRange(new MapRange(0, 22));

        // Set route listener
        RouteMapEventListener mapListener = new RouteMapEventListener();
        getMapView().setMapEventListener(mapListener);

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

        // Style for instruction balloons
        balloonPopupStyleBuilder = new BalloonPopupStyleBuilder();
        balloonPopupStyleBuilder.setTitleMargins(new BalloonPopupMargins(4, 4, 4, 4));
    }

    protected MapView getMapView() {
        return mapView;
    }

    protected void addBaseLayer() {
        // Add online base layer, even used in Offline routing example
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
    }

    protected RoutingService service;

    public RoutingResult getResult(MapPos start, MapPos stop) {
        MapPosVector poses = new MapPosVector();

        poses.add(start);
        poses.add(stop);

        RoutingRequest request = new RoutingRequest(getMapView().getOptions().getBaseProjection(), poses);

        try {
            return service.calculateRoute(request);
        } catch (Exception e) {
            // Can return IOException and RuntimeException
            return null;
        }
    }

    public void showRoute(final MapPos startPos, final MapPos stopPos) {

        AsyncTask<Void, Void, RoutingResult> task = new AsyncTask<Void, Void, RoutingResult>() {

            long timeStart;

            protected RoutingResult doInBackground(Void... v) {
                timeStart = System.currentTimeMillis();
                RoutingResult result = getResult(startPos, stopPos);

                return result;
            }

            protected void onPostExecute(RoutingResult result) {

                if (result == null) {
                    alert("Routing failed");
                    return;
                }

                String routeText =
                        "The route is " + (int) (result.getTotalDistance() / 100) / 10f
                        + "km (" + secondsToHours((int) result.getTotalTime())
                        + ") calculation: " + (System.currentTimeMillis() - timeStart) + " ms";

                alert(routeText);

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
                        createRoutePoint(position, instruction.getAction(), routeDataSource);
                    }

                }
            }
        };

        task.execute();
    }

    protected void createRoutePoint(MapPos pos, RoutingAction action, LocalVectorDataSource ds) {

        MarkerStyle style = instructionUp;
        String str = "";

        switch (action) {
            case ROUTING_ACTION_HEAD_ON:
                str = "head on";
                break;
            case ROUTING_ACTION_FINISH:
                str = "finish";
                break;
            case ROUTING_ACTION_TURN_LEFT:
                style = instructionLeft;
                str = "turn left";
                break;
            case ROUTING_ACTION_TURN_RIGHT:
                style = instructionRight;
                str = "turn right";
                break;
            case ROUTING_ACTION_UTURN:
                str = "u turn";
                break;
            case ROUTING_ACTION_NO_TURN:
            case ROUTING_ACTION_GO_STRAIGHT:
                // Do nothing
                break;
            case ROUTING_ACTION_REACH_VIA_LOCATION:
                style = instructionUp;
                str = "stopover";
                break;
            case ROUTING_ACTION_ENTER_AGAINST_ALLOWED_DIRECTION:
                str = "enter against allowed direction";
                break;
            case ROUTING_ACTION_LEAVE_AGAINST_ALLOWED_DIRECTION:
                break;
            case ROUTING_ACTION_ENTER_ROUNDABOUT:
                str = "enter roundabout";
                break;
            case ROUTING_ACTION_STAY_ON_ROUNDABOUT:
                str = "stay on roundabout";
                break;
            case ROUTING_ACTION_LEAVE_ROUNDABOUT:
                str = "leave roundabout";
                break;
            case ROUTING_ACTION_START_AT_END_OF_STREET:
                str = "start at end of street";
                break;
        }

        if (!str.equals("")){
            Marker marker = new Marker(pos, style);
            BalloonPopup popup2 = new BalloonPopup(marker, balloonPopupStyleBuilder.buildStyle(),
                    str, "");
            ds.add(popup2);
            ds.add(marker);
        }
    }

    protected Line createPolyline(RoutingResult result) {

        LineStyleBuilder lineStyleBuilder = new LineStyleBuilder();
        lineStyleBuilder.setColor(new com.carto.graphics.Color(Color.DKGRAY));
        lineStyleBuilder.setWidth(12);

        return new Line(result.getPoints(), lineStyleBuilder.buildStyle());
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
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeResource(getResources(), id);
        return BitmapUtils.createBitmapFromAndroidBitmap(bitmap);
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

    /**
     * This MapListener waits for two clicks on map - first to set routing start point, and then
     * second to mark end point and start routing service.
     */
    public class RouteMapEventListener extends MapEventListener {
        private MapPos startPos;
        private MapPos stopPos;

        // Map View manipulation handlers
        @Override
        public void onMapClicked(MapClickInfo mapClickInfo) {

            if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {

                MapPos clickPos = mapClickInfo.getClickPos();

                if (startPos == null) {

                    // set start, or start again
                    startPos = clickPos;
                    setStartMarker(clickPos);

                } else if (stopPos == null) {

                    // set stop and calculate
                    stopPos = clickPos;
                    setStopMarker(clickPos);
                    showRoute(startPos, stopPos);

                    // restart to force new route next time
                    startPos = null;
                    stopPos = null;
                }
            }
        }

        @Override
        public void onMapMoved() {
        }
    }
}
