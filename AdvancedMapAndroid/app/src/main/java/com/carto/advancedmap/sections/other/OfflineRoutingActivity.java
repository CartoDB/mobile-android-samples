package com.carto.advancedmap.sections.other;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.carto.advancedmap.MapApplication;
import com.carto.advancedmap.baseactivities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.R;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.VectorLayer;
import com.carto.packagemanager.CartoPackageManager;
import com.carto.packagemanager.PackageAction;
import com.carto.packagemanager.PackageErrorType;
import com.carto.packagemanager.PackageManager;
import com.carto.packagemanager.PackageManagerListener;
import com.carto.packagemanager.PackageStatus;
import com.carto.routing.CartoOnlineRoutingService;
import com.carto.routing.PackageManagerRoutingService;
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
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Marker;

@ActivityData(name = "Offline Routing", description = "Routing with OpenStreetMap data packages")
public class OfflineRoutingActivity extends MapBaseActivity {


    // add packages what you want to download
    private static String[] PACKAGE_IDS = new String[]{"EE-routing",
            "LT-routing"};

    private static String ROUTING_PACKAGEMANAGER_SOURCE = "routing:nutiteq.osm.car";
    private static String ROUTING_SERVICE_SOURCE = "nutiteq.osm.car";


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
			MapPos clickPos = mapClickInfo.getClickPos();
			MapPos wgs84Clickpos = mapView.getOptions().getBaseProjection().toWgs84(clickPos);
			Log.d(MapApplication.LOG_TAG,"onMapClicked " + wgs84Clickpos + " " + mapClickInfo.getClickType());

			if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
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

	/**
	 * Listener for package manager events.
	 */
	class PackageListener extends PackageManagerListener {
    	@Override
    	public void onPackageListUpdated() {
			Log.d(MapApplication.LOG_TAG, "Package list updated");

            int downloadedPackages = 0;
            for (int i=0; i<PACKAGE_IDS.length;i++) {
                boolean alreadyDownloaded = getPackageIfNotExists(PACKAGE_IDS[i]);
                if(alreadyDownloaded){
                    downloadedPackages ++;
                }
            }

            // if all downloaded, can start with offline routing
            if (downloadedPackages == PACKAGE_IDS.length) {
                offlinePackageReady = true;
            }
    	}

        private boolean getPackageIfNotExists(String packageId) {
            PackageStatus status = packageManager.getLocalPackageStatus(packageId, -1);
            if (status == null) {
                packageManager.startPackageDownload(packageId);
                return false;
            }else if(status.getCurrentAction() == PackageAction.PACKAGE_ACTION_READY){
                Log.d(MapApplication.LOG_TAG, packageId + " is downloaded and ready");
                return true;
            }

            return false;
        }

        @Override
		public void onPackageListFailed() {
			Log.e(MapApplication.LOG_TAG, "Package list update failed");
		}

		@Override
		public void onPackageStatusChanged(String id, int version, PackageStatus status) {
		}

		@Override
		public void onPackageCancelled(String id, int version) {
		}

    	@Override
    	public void onPackageUpdated(String id, int version) {
			Log.d(MapApplication.LOG_TAG, "Offline package updated: " + id);
            // if last downloaded
    		if (id.equals(PACKAGE_IDS[PACKAGE_IDS.length-1])) {
    			offlinePackageReady = true;        			
    		}
    	}

		@Override
		public void onPackageFailed(String id, int version, PackageErrorType errorType) {
			Log.e(MapApplication.LOG_TAG, "Offline package update failed: " + id);
		}
	}


    private RoutingService onlineRoutingService;
    private RoutingService offlineRoutingService;
    private PackageManager packageManager;
    private boolean shortestPathRunning;
    private boolean offlinePackageReady;
    private Marker startMarker;
    private Marker stopMarker;
    private MarkerStyle instructionUp;
    private MarkerStyle instructionLeft;
    private MarkerStyle instructionRight;
    private LocalVectorDataSource routeDataSource;
    private LocalVectorDataSource routeStartStopDataSource;
    private BalloonPopupStyleBuilder balloonPopupStyleBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add default base layer
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);

        // Create PackageManager instance for dealing with offline packages
        File packageFolder = new File(getApplicationContext().getExternalFilesDir(null), "routingpackages");

        if (!(packageFolder.mkdirs() || packageFolder.isDirectory())) {
        	Log.e(MapApplication.LOG_TAG, "Could not create package folder!");
        }
        try {
            packageManager = new CartoPackageManager(ROUTING_PACKAGEMANAGER_SOURCE, packageFolder.getAbsolutePath());
        }
        catch (IOException e) {
            Log.e(MapApplication.LOG_TAG, "Exception: " + e);
            finish();
        }

        packageManager.setPackageManagerListener(new PackageListener());
        packageManager.start();

        // fetch list of available packages from server.
        // This is asynchronous and listener will be notified via onPackageListUpdated when this succeeds.
        packageManager.startPackageListDownload();
        
        // Create offline routing service connected to package manager
        offlineRoutingService = new PackageManagerRoutingService(packageManager);
        
        // Create additional online routing service that will be used when offline package
        // is not yet downloaded or offline routing fails
        onlineRoutingService = new CartoOnlineRoutingService(ROUTING_SERVICE_SOURCE);

        // Define layer and datasource for route line and instructions
        routeDataSource = new LocalVectorDataSource(baseProjection);
        VectorLayer routeLayer = new VectorLayer(routeDataSource);
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
        RouteMapEventListener mapListener = new RouteMapEventListener();
        mapView.setMapEventListener(mapListener);

        // Create markers for start & end, and a layer for them
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setBitmap(getBitmapFromResource(R.drawable.olmarker));
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
        markerStyleBuilder.setBitmap(getBitmapFromResource(R.drawable.direction_up));
        instructionUp = markerStyleBuilder.buildStyle();

        markerStyleBuilder.setBitmap(getBitmapFromResource(R.drawable.direction_upthenleft));
        instructionLeft = markerStyleBuilder.buildStyle();

        markerStyleBuilder.setBitmap(getBitmapFromResource(R.drawable.direction_upthenright));

        instructionRight = markerStyleBuilder.buildStyle();
        
        // Style for instruction balloons
        balloonPopupStyleBuilder = new BalloonPopupStyleBuilder();
        balloonPopupStyleBuilder.setTitleMargins(new BalloonPopupMargins(4, 4, 4, 4));
        
        // Animate map to Estonia
        mapView.setFocusPos(mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(25.662893, 58.919365)), 0);
        mapView.setZoom(7, 0);

        String message = "Long-press on map to set route start and finish";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    com.carto.graphics.Bitmap getBitmapFromResource(int resource) {
        Bitmap androidBitmap = BitmapFactory.decodeResource(getResources(), resource);
        return BitmapUtils.createBitmapFromAndroidBitmap(androidBitmap);
    }

    public void showRoute(final MapPos startPos, final MapPos stopPos) {
    	
        Log.d(MapApplication.LOG_TAG, "calculating path " + startPos + " to " + stopPos);

        if (!offlinePackageReady) {
        	runOnUiThread(new Runnable() {
        		@Override
        		public void run() {
                    String message = "Offline package is not ready, using online routing";
        			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        		}
        	});
        }

        AsyncTask<Void, Void, RoutingResult> task = new AsyncTask<Void, Void, RoutingResult>() {

            long timeStart;

            protected RoutingResult doInBackground(Void... v) {

                timeStart = System.currentTimeMillis();
                MapPosVector poses = new MapPosVector();

                poses.add(startPos);
                poses.add(stopPos);

                RoutingRequest request = new RoutingRequest(mapView.getOptions().getBaseProjection(), poses);
                RoutingResult result;

                try {
                    if (offlinePackageReady) {
                        result = offlineRoutingService.calculateRoute(request);
                    } else {
                        result = onlineRoutingService.calculateRoute(request);
                    }
                } catch (Exception e) {
                    result = null;
                }

                return result;
            }

            protected void onPostExecute(RoutingResult result) {

                if (result == null) {
                    Toast.makeText(getApplicationContext(), "Routing failed", Toast.LENGTH_LONG).show();
                    shortestPathRunning = false;
                    return;
                }

                String routeText = "The route is " + (int) (result.getTotalDistance() / 100) / 10f
                        + "km (" + secondsToHours((int) result.getTotalTime())
                        + ") calculation: " + (System.currentTimeMillis() - timeStart) + " ms";
                Log.i(MapApplication.LOG_TAG,routeText);
                Toast.makeText(getApplicationContext(), routeText, Toast.LENGTH_LONG).show();

                routeDataSource.clear();

                startMarker.setVisible(false);

                routeDataSource.add(createPolyline(result));

                // Add instruction markers
                RoutingInstructionVector instructions = result.getInstructions();
                boolean first = true;

                for (int i = 0; i < instructions.size(); i++) {
                	RoutingInstruction instruction = instructions.get(i);
                    if (first) {
                        Log.d(MapApplication.LOG_TAG,"first instruction");
                        // Set car to first instruction position
                        first = false;
                    } else {
                        MapPos position = result.getPoints().get(instruction.getPointIndex());
                        createRoutePoint(position, instruction.getAction(), routeDataSource);
                    }

                }

                shortestPathRunning = false;
            }
        };

        if (!shortestPathRunning) {
        	shortestPathRunning = true;
        	task.execute();
        }
    }

    protected String secondsToHours(int sec) {
        int hours = sec / 3600,
                remainder = sec % 3600,
                minutes = remainder / 60,
                seconds = remainder % 60;

        return ( (hours < 10 ? "0" : "") + hours
                + "h" + (minutes < 10 ? "0" : "") + minutes
                + "m" + (seconds< 10 ? "0" : "") + seconds+"s" );
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

    // Creates line from GraphHopper response
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

}
