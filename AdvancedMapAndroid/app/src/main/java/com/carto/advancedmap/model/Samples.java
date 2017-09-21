package com.carto.advancedmap.model;

import com.carto.advancedmap.sections.basemap.BaseMapActivity;
import com.carto.advancedmap.sections.geocoding.offline.GeoPackageDownloadActivity;
import com.carto.advancedmap.sections.geocoding.offline.ReverseGeoPackageDownloadActivity;
import com.carto.advancedmap.sections.geocoding.online.OnlineGeocodingActivity;
import com.carto.advancedmap.sections.geocoding.online.OnlineReverseGeocodingActivity;
import com.carto.advancedmap.sections.offlinemap.BundledMapActivity;
import com.carto.advancedmap.sections.offlinemap.advancedpackagemanager.OfflineMapActivity;
import com.carto.advancedmap.sections.other.CaptureActivity;
import com.carto.advancedmap.sections.other.CustomPopupActivity;
import com.carto.advancedmap.sections.other.GPSLocationActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomRasterDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.CustomVectorDataSourceActivity;
import com.carto.advancedmap.sections.overlaydatasources.GroundOverlayActivity;
import com.carto.advancedmap.sections.overlaydatasources.WmsMapActivity;
import com.carto.advancedmap.sections.routing.OnlineRoutingActivity;
import com.carto.advancedmap.sections.routing.offline.OfflineRoutingPackageActivity;
import com.carto.advancedmap.sections.vectorobjects.ClusteredMarkersActivity;
import com.carto.advancedmap.sections.vectorobjects.OverlaysActivity;
import com.carto.advancedmap.sections.vectorobjects.VectorObjectEditingActivity;

/**
 * Created by aareundo on 21/09/2017.
 */

public class Samples {

    public static Sample[] LIST = {

            new Sample(0,
                    "Base Maps",
                    "Choice between different base maps, styles, languages",
                    BaseMapActivity.class
            ),
            new Sample(0,
                    "Offline Map",
                    "Download offline map packages",
                    OfflineMapActivity.class
            ),
            new Sample(0,
                    "Bundled Map",
                    "Offline map of Rome bundled with app",
                    BundledMapActivity.class
            ),
            new Sample(0,
                    "Online Routing",
                    "Online routing with Open Street Map data packages",
                    OnlineRoutingActivity.class
            ),
            new Sample(0,
                    "Offline Routing",
                    "Download routing packages for offline use",
                    OfflineRoutingPackageActivity.class
            ),
            new Sample(0,
                    "Online Reverse Geocoding",
                    "Online reverse geocoding with Pelias geocoder",
                    OnlineReverseGeocodingActivity.class
            ),
            new Sample(0,
                    "Online Geocoding",
                    "Online geocoding with Pelias geocoder",
                    OnlineGeocodingActivity.class
            ),
            new Sample(0,
                    "Offline Reverse Geocoding",
                    "Download offline geocoding packages",
                    ReverseGeoPackageDownloadActivity.class
            ),
            new Sample(0,
                    "Offline Geocoding",
                    "Download offline geocoding packages",
                    GeoPackageDownloadActivity.class
            ),

            new Sample(0,
                    "Custom Raster Data Source",
                    "Customized raster tile data source",
                    CustomRasterDataSourceActivity.class
            ),
            new Sample(0,
                    "Custom Vector Data Source",
                    "Customized vector data source",
                    CustomVectorDataSourceActivity.class
            ),
            new Sample(0,
                    "Ground Overlays",
                    "Show non-tiled Bitmap on ground",
                    GroundOverlayActivity.class
            ),
            new Sample(0,
                    "WMS Map",
                    "Use external WMS service for raster tile overlay",
                    WmsMapActivity.class
            ),

            new Sample(0,
                    "Clustered Markers",
                    "Show 20,000 points from geojson",
                    ClusteredMarkersActivity.class
            ),

            new Sample(0,
                    "Overlays",
                    "2D and 3D objects: lines, points, polygons, texts, pop-ups and a NMLModel",
                    OverlaysActivity.class
            ),

            new Sample(0,
                    "Vector Object Editing",
                    "Shows usage of an editable vector layer",
                    VectorObjectEditingActivity.class
            ),

            new Sample(0,
                    "Screencapture",
                    "Capture rendered mapView as a Bitmap",
                    CaptureActivity.class
            ),

            new Sample(0,
                    "Custom Popup",
                    "Creates a custom popup",
                    CustomPopupActivity.class
            ),

            new Sample(0,
                    "GPS Location",
                    "Shows user GPS location on the map",
                    GPSLocationActivity.class
            ),
    };
}
