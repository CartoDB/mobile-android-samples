package com.carto.advancedmap.sections.routing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.carto.advanced.kotlin.components.PopupButton;
import com.carto.advancedmap.R;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.baseclasses.views.MapBaseView;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.Variant;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.FeatureCollection;
import com.carto.geometry.GeoJSONGeometryReader;
import com.carto.graphics.Bitmap;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.VectorElementEventListener;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.routing.RoutingAction;
import com.carto.routing.RoutingInstruction;
import com.carto.routing.RoutingInstructionVector;
import com.carto.routing.RoutingRequest;
import com.carto.routing.RoutingResult;
import com.carto.routing.RoutingService;
import com.carto.routing.SGREOfflineRoutingService;
import com.carto.styles.GeometryCollectionStyleBuilder;
import com.carto.styles.LineStyle;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.ui.ClickType;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.VectorElementClickInfo;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.carto.routing.RoutingAction.ROUTING_ACTION_GO_DOWN;
import static com.carto.routing.RoutingAction.ROUTING_ACTION_GO_UP;
import static com.carto.routing.RoutingAction.ROUTING_ACTION_TURN_LEFT;
import static com.carto.routing.RoutingAction.ROUTING_ACTION_TURN_RIGHT;

/**
 * Created by aareundo on 08/11/16.
 */

public class IndoorRoutingActivity extends MapBaseActivity {
    private static final float FLOOR_HEIGHT = 10.0f;

    private RoutingService service;

    private int selectedFloor = 0;

    private MapPos stopPos;
    private MapPos startPos;

    private List<VectorLayer> floorLayers = new ArrayList<VectorLayer>();
    private List<VectorLayer> routeLayers = new ArrayList<VectorLayer>();

    private Marker startMarker;
    private Marker stopMarker;
    private MarkerStyle instructionUpStyle;
    private MarkerStyle instructionDownStyle;
    private MarkerStyle instructionLeftStyle;
    private MarkerStyle instructionRightStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView.addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);

        contentView.mapView.setMapEventListener(new RouteMapClickListener());

        // Initialize layers/styles
        createMarkers();
        for (int i = 0; i < 2; i++) {
            final int level = i;

            String geojson = loadJSONFromAsset("floor" + level + ".geojson");
            VectorLayer floorLayer = createFloorLayer(geojson, level);
            floorLayers.add(floorLayer);

            VectorLayer routeLayer = createRouteLayer();
            routeLayers.add(routeLayer);

            PopupButton button = new PopupButton(contentView.getContext(), level > 0 ? R.drawable.icon_2 : R.drawable.icon_1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectFloor(level);
                }
            });
            contentView.addButton(button);
        }

        // Create routing service
        Variant config = Variant.fromString(loadJSONFromAsset("sgreconfig.json"));
        Variant routingGeoJSON = Variant.fromString(loadJSONFromAsset("floors_routing.geojson"));
        try {
            service = new SGREOfflineRoutingService(routingGeoJSON, config);
        } catch (IOException ex) {
            alert("Failed to initialize routing");
        }

        // Initialize map
        Projection proj = contentView.mapView.getOptions().getBaseProjection();
        selectFloor(0);
        contentView.mapView.setFocusPos(proj.fromLatLong(58.366, 26.744), 0);
        contentView.mapView.setZoom(15.5f, 0);
        contentView.mapView.setZoom(16.0f, 0);

        contentView.layoutSubviews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (VectorLayer routeLayer : routeLayers) {
            routeLayer.setVectorElementEventListener(null);
        }
        for (VectorLayer floorLayer : floorLayers) {
            floorLayer.setVectorElementEventListener(null);
        }
        contentView.mapView.setMapEventListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        contentView.banner.alert("Long click on the map to set route start/stop position.");
    }

    private String loadJSONFromAsset(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            alert("Failed to load file: " + fileName);
            return null;
        }
    }

    void createMarkers() {
        // Create markers for start and end
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setSize(30);
        markerStyleBuilder.setHideIfOverlapped(false);
        markerStyleBuilder.setColor(new com.carto.graphics.Color(0xFF00FF00));

        // Initial empty markers
        startMarker = new Marker(new MapPos(0, 0, 0), markerStyleBuilder.buildStyle());

        // Change color to Red
        markerStyleBuilder.setColor(new com.carto.graphics.Color(0xFFFF0000));

        stopMarker = new Marker(new MapPos(0, 0, 0), markerStyleBuilder.buildStyle());

        // Create styles for instruction markers
        markerStyleBuilder.setColor(new com.carto.graphics.Color(0xFFFFFFFF));
        markerStyleBuilder.setBitmap(createBitmap(R.drawable.direction_up));
        instructionUpStyle = markerStyleBuilder.buildStyle();

        markerStyleBuilder.setBitmap(createBitmap(R.drawable.direction_down));
        instructionDownStyle = markerStyleBuilder.buildStyle();

        markerStyleBuilder.setBitmap(createBitmap(R.drawable.direction_upthenleft));
        instructionLeftStyle = markerStyleBuilder.buildStyle();

        markerStyleBuilder.setBitmap(createBitmap(R.drawable.direction_upthenright));
        instructionRightStyle = markerStyleBuilder.buildStyle();
    }

    private void selectFloor(int level) {
        for (int i = 0; i < floorLayers.size(); i++) {
            floorLayers.get(i).setVisible(i == level);
            routeLayers.get(i).setVisible(i == level);
        }
        selectedFloor = level;
    }

    private void setStart(MapPos mapPos) {
        for (VectorLayer routeLayer : routeLayers) {
            LocalVectorDataSource routeDataSource = (LocalVectorDataSource) routeLayer.getDataSource();
            routeDataSource.clear();
        }
        startPos = new MapPos(mapPos.getX(), mapPos.getY(), selectedFloor * FLOOR_HEIGHT);
        startMarker.setPos(mapPos);

        LocalVectorDataSource routeDataSource = (LocalVectorDataSource)routeLayers.get(selectedFloor).getDataSource();
        routeDataSource.add(startMarker);
    }

    private void setStop(MapPos mapPos) {
        stopPos = new MapPos(mapPos.getX(), mapPos.getY(), selectedFloor * FLOOR_HEIGHT);
        stopMarker.setPos(mapPos);
        LocalVectorDataSource routeDataSource = (LocalVectorDataSource)routeLayers.get(selectedFloor).getDataSource();
        routeDataSource.add(stopMarker);
        showRoute(startPos, stopPos);
    }

    protected void showRoute(final MapPos startPos, final MapPos stopPos) {

        AsyncTask<Void, Void, RoutingResult> task = new AsyncTask<Void, Void, RoutingResult>() {

            @Override
            protected RoutingResult doInBackground(Void... v) {

                RoutingResult result = getResult(startPos, stopPos);

                return result;
            }

            @Override
            protected void onPostExecute(RoutingResult result) {

                if (result == null) {
                    alert("Routing failed");
                    return;
                }

                // Show line
                LineStyleBuilder lineStyleBuilder = new LineStyleBuilder();
                lineStyleBuilder.setColor(new com.carto.graphics.Color(0xFF888888));
                lineStyleBuilder.setWidth(12.0f);
                LineStyle lineStyle = lineStyleBuilder.buildStyle();

                // Show route segments. Split them according to floor.
                MapPosVector points = result.getPoints();
                int i0 = 0, i1 = 0;
                for (; i1 <= points.size(); i1++) {
                    int level0 = (int) (points.get(i0).getZ() / FLOOR_HEIGHT);
                    int level1 = (i1 < points.size() ? (int) (points.get(i1).getZ() / FLOOR_HEIGHT) : -1);
                    if (level0 != level1) {
                        MapPosVector routePoses = new MapPosVector();
                        for (int i = i0; i < i1; i++) {
                            MapPos mapPos3D = points.get(i);
                            routePoses.add(new MapPos(mapPos3D.getX(), mapPos3D.getY(), 0));
                        }
                        Line routeLine = new Line(routePoses, lineStyle);
                        LocalVectorDataSource dataSource = (LocalVectorDataSource) routeLayers.get(level0).getDataSource();
                        dataSource.add(routeLine);

                        i0 = i1;
                    }
                }

                // Add instruction markers
                RoutingInstructionVector instructions = result.getInstructions();
                for (int i = 0; i < instructions.size(); i++) {
                    RoutingInstruction instruction = instructions.get(i);

                    MapPos mapPos3D = points.get(instruction.getPointIndex());
                    MapPos mapPos = new MapPos(mapPos3D.getX(), mapPos3D.getY(), 0);
                    int level = (int)(mapPos3D.getZ() / FLOOR_HEIGHT);

                    Marker marker = createRoutePoint(mapPos, instruction);
                    if (marker != null) {
                        LocalVectorDataSource dataSource = (LocalVectorDataSource) routeLayers.get(level).getDataSource();
                        dataSource.add(marker);
                    }
                }
            }
        };

        task.execute();
    }

    private RoutingResult getResult(MapPos start, MapPos stop) {
        MapPosVector poses = new MapPosVector();

        poses.add(start);
        poses.add(stop);

        RoutingRequest request = new RoutingRequest(contentView.mapView.getOptions().getBaseProjection(), poses);

        try {
            return service.calculateRoute(request);
        } catch (Exception e) {
            // Can return IOException and RuntimeException
            return null;
        }
    }

    private VectorLayer createFloorLayer(String geojson, int level) {
        Projection proj = contentView.mapView.getOptions().getBaseProjection();

        // Load feature collection
        GeoJSONGeometryReader reader = new GeoJSONGeometryReader();
        reader.setTargetProjection(proj);
        FeatureCollection collection = reader.readFeatureCollection(geojson);

        // Initialize style
        GeometryCollectionStyleBuilder builder = new GeometryCollectionStyleBuilder();
        PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
        pointStyleBuilder.setColor(new com.carto.graphics.Color((short)0, (short)(level*80), (short)(255-level*80), (short)255));
        pointStyleBuilder.setSize(5.0f);
        builder.setPointStyle(pointStyleBuilder.buildStyle());
        LineStyleBuilder lineStyleBuilder = new LineStyleBuilder();
        lineStyleBuilder.setColor(new com.carto.graphics.Color((short)0, (short)(level*80), (short)(255-level*80), (short)255));
        lineStyleBuilder.setWidth(3.0f);
        builder.setLineStyle(lineStyleBuilder.buildStyle());

        PolygonStyleBuilder polygonStyleBuilder = new PolygonStyleBuilder();
        polygonStyleBuilder.setColor(new com.carto.graphics.Color((short)0, (short)(level*80), (short)(255-level*80), (short)128));
        polygonStyleBuilder.setLineStyle(lineStyleBuilder.buildStyle());
        builder.setPolygonStyle(polygonStyleBuilder.buildStyle());

        // Create data source and layer
        LocalVectorDataSource dataSource = new LocalVectorDataSource(proj);
        VectorLayer vectorLayer = new VectorLayer(dataSource);
        dataSource.addFeatureCollection(collection, builder.buildStyle());

        // Add listener
        vectorLayer.setVectorElementEventListener(new RouteVectorElementClickListener());

        // Add the previous vector layer to the map
        contentView.mapView.getLayers().add(vectorLayer);

        return vectorLayer;
    }

    private VectorLayer createRouteLayer() {
        Projection proj = contentView.mapView.getOptions().getBaseProjection();

        LocalVectorDataSource dataSource = new LocalVectorDataSource(proj);
        VectorLayer vectorLayer = new VectorLayer(dataSource);

        // Add the previous vector layer to the map
        contentView.mapView.getLayers().add(vectorLayer);

        return vectorLayer;
    }

    private Marker createRoutePoint(MapPos pos, RoutingInstruction instruction) {
        RoutingAction action = instruction.getAction();

        MarkerStyle style = null;
        if (action == ROUTING_ACTION_TURN_LEFT) {
            style = instructionLeftStyle;
        } else if (action == ROUTING_ACTION_TURN_RIGHT) {
            style = instructionRightStyle;
        } else if (action == ROUTING_ACTION_GO_DOWN) {
            style = instructionDownStyle;
        } else if (action == ROUTING_ACTION_GO_UP) {
            style = instructionUpStyle;
        } else {
            return null;
        }

        return new Marker(pos, style);
    }

    private Bitmap createBitmap(int id) {
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeResource(contentView.getContext().getResources(), id);
        return BitmapUtils.createBitmapFromAndroidBitmap(bitmap);
    }

    class RouteMapClickListener extends MapEventListener {
        private MapPos startPos = null;
        private MapPos stopPos = null;

        @Override
        public void onMapClicked(MapClickInfo clickInfo) {
            MapPos clickPos = clickInfo.getClickPos();

            if (clickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
                if (startPos == null) {
                    startPos = clickPos;
                    setStart(clickPos);
                } else {
                    stopPos = clickPos;
                    setStop(clickPos);

                    // restart to force new route next time
                    startPos = clickPos = null;
                }
            }
        }
    }

    class RouteVectorElementClickListener extends VectorElementEventListener {
        @Override
        public boolean onVectorElementClicked(VectorElementClickInfo clickInfo) {
            if (clickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
                return false; // pass handling to MapEventListener
            }

            return true;
        }
    }
}
