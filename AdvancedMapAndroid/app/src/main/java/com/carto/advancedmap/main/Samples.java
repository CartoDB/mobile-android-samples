package com.carto.advancedmap.main;

import com.carto.advancedmap.R;
import com.carto.advancedmap.sections.basemap.BaseMapActivity;
import com.carto.advancedmap.sections.geocoding.offline.OfflineGeocodingActivity;
import com.carto.advancedmap.sections.geocoding.offline.OfflineReverseGeocodingActivity;
import com.carto.advancedmap.sections.geocoding.online.OnlineGeocodingActivity;
import com.carto.advancedmap.sections.geocoding.online.OnlineReverseGeocodingActivity;
import com.carto.advancedmap.sections.offlinemap.BundledMapActivity;
import com.carto.advancedmap.sections.offlinemap.OfflineMapActivity;
import com.carto.advancedmap.sections.other.CaptureActivity;
import com.carto.advancedmap.sections.other.CustomPopupActivity;
import com.carto.advancedmap.sections.other.GPSLocationActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomRasterDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomVectorDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.GroundOverlayActivity;
import com.carto.advancedmap.sections.overlaydatasources.WmsMapActivity;
import com.carto.advancedmap.sections.routing.OnlineRoutingActivity;
import com.carto.advancedmap.sections.routing.OfflineRoutingActivity;
import com.carto.advancedmap.sections.vectorobjects.ClusteredMarkersActivity;
import com.carto.advancedmap.sections.vectorobjects.OverlaysActivity;
import com.carto.advancedmap.sections.vectorobjects.VectorObjectEditingActivity;

/**
 * Created by aareundo on 21/09/2017.
 */

public class Samples {

    public static Sample[] LIST = {

            new Sample(
                    R.drawable.background_basemaps,
                    "Online Base Maps",
                    "Choice between different base maps, styles, languages",
                    BaseMapActivity.class
            ),
            new Sample(
                    R.drawable.background_advanced_package_manager,
                    "Offline Map",
                    "Download offline map packages",
                    OfflineMapActivity.class
            ),
            new Sample(
                    R.drawable.background_bundled_map,
                    "Bundled Map",
                    "Offline map of Rome bundled with app",
                    BundledMapActivity.class
            ),
            new Sample(
                    R.drawable.background_routing_online,
                    "Online Routing",
                    "Online routing using Mapzen service",
                    OnlineRoutingActivity.class
            ),
            new Sample(
                    R.drawable.background_routing_offline,
                    "Offline Routing",
                    "Download routing packages for offline use",
                    OfflineRoutingActivity.class
            ),
            new Sample(
                    R.drawable.background_online_reverse_geocoding,
                    "Online Reverse Geocoding",
                    "Online reverse geocoding with Pelias geocoder",
                    OnlineReverseGeocodingActivity.class
            ),
            new Sample(
                    R.drawable.background_online_geocoding,
                    "Online Geocoding",
                    "Online geocoding with Pelias geocoder",
                    OnlineGeocodingActivity.class
            ),
            new Sample(
                    R.drawable.background_offline_reverse_geocoding,
                    "Offline Reverse Geocoding",
                    "Download offline geocoding packages",
                    OfflineReverseGeocodingActivity.class
            ),
            new Sample(
                    R.drawable.background_offline_geocoding,
                    "Offline Geocoding",
                    "Download offline geocoding packages",
                    OfflineGeocodingActivity.class
            ),
            new Sample(
                    R.drawable.background_custom_raster_source,
                    "Custom Raster Data Source",
                    "Customized raster tile data source",
                    CustomRasterDataSourceActivity.class
            ),
            new Sample(
                    R.drawable.background_custom_verctor_source,
                    "Custom Vector Data Source",
                    "Customized vector data source",
                    CustomVectorDataSourceActivity.class
            ),
            new Sample(
                    R.drawable.background_ground_overlay,
                    "Ground Overlays",
                    "Show non-tiled Bitmap on ground",
                    GroundOverlayActivity.class
            ),
            new Sample(
                    R.drawable.background_wms,
                    "WMS Map",
                    "Use external WMS service for raster tile overlay",
                    WmsMapActivity.class
            ),

            new Sample(
                    R.drawable.background_clusters,
                    "Clustered Markers",
                    "Show 20,000 points from geojson",
                    ClusteredMarkersActivity.class
            ),

            new Sample(
                    R.drawable.background_overlays,
                    "Overlays",
                    "2D and 3D objects: lines, points, polygons, texts, pop-ups and a NMLModel",
                    OverlaysActivity.class
            ),
            new Sample(
                    R.drawable.background_object_editing,
                    "Vector Object Editing",
                    "Shows usage of an editable vector layer",
                    VectorObjectEditingActivity.class
            ),

            new Sample(
                    R.drawable.background_screen_capture,
                    "Screencapture",
                    "Capture rendered mapView as a Bitmap",
                    CaptureActivity.class
            ),

            new Sample(
                    R.drawable.background_custom_popup,
                    "Custom Popup",
                    "Creates a custom popup",
                    CustomPopupActivity.class
            ),

            new Sample(
                    R.drawable.background_gps_location,
                    "GPS Location",
                    "Shows user GPS location on the map",
                    GPSLocationActivity.class
            ),
    };
}
