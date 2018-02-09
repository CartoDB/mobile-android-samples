package com.carto.advanced.kotlin.model

/**
 * Created by aareundo on 10/07/2017.
 */
class Texts {

    companion object {

        val basemapInfoHeader = "CARTO BASE MAPS"

        val basemapInfoContainer =
                "CARTO offers a variety of different base map styles, here are a few samples:\n \n" +
                        "Voyager is your classic style, with a new polish, used " +
                        "mainly for routing, navigation etc.\n \n" +
                        "Positron is a bit more humble, shades of light gray, for when you need" +
                        " to display information on top of your base map: great for colorful visualizations.\n \n" +
                        "Darkmatter is dark, heavy, like the night, it's for when you're feeling extra frisky," +
                        " when you want to walk on the wild side or... " +
                        "just for visualizations that work better with a dark background."

        val routeSearchInfoHeader = "ROUTE SEARCH"

        val routeSearchInfoContainer =
                "CARTOMobileSDK 4.1.0 supports searching from various sources. " +
                        "This sample combines online routing with online searching from vector tiles. \n\n" +
                        "To see this in action, just click on two different points. The sample will first " +
                        "calculate the fastest route between the points and then uses search APIs to find " +
                        "all attractions along the route within 500 meters. These Points of Interests are then " +
                        "shown as clickable red dots.\n\n"

        val packageDownloadInfoHeader = "OFFLINE MAP"

        val packageDownloadInfoContainer =
                "This example lets you download pre-defined packages. The packages are mostly " +
                        "country-based, but some larger countries, like The United States, " +
                        "Russia and Germany, are municipality (or oblast etc.) based.\n\n" +
                        "Simply choose a package, press download and it will start, " +
                        "the progress will be displayed in the list and on the map.\n\n" +
                        "Our SDK also offers the option to pause, resume he download and, when you have it, " +
                        "the option to delete the package.\n\n" +
                        "The packages are defined by CARTO's Mobile team and are readily available in our mobile SDK's API."

        val vectorElementsInfoHeader = "VECTOR OBJECTS"

        val vectorElementsInfoContainer =
                "With CARTOMobileSDK you have a convenient way to add different vector elements to your map to spice it up.\n\n" +
                        "These are just a few examples of vector elements provided by us. " +
                        "We have a variety of different popups, markers, points, lines, polygons, and models.\n\n" +
                        "All elements are also interactive. Go ahead, click on one!\n\n" +
                        "In this example, the nml model is a file bundled with the app. NML is CARTO's own compact format for 2D and 3D models.\n\n"

        val clusteringInfoHeader = "VECTOR ELEMENT CLUSTERING"

        val clusteringInfoContainer =
                "CARTO offers a convenient way to build clusters.\n\n" +
                        "Here's an example of the clustering of languages with over 15000 residents, " +
                        "which is approximately 20,000 languages around the world.\n\n" +
                        "CARTO's Mobile SDK creates these clusters in a matter of milliseconds " +
                        "and animates them to place when zooming in, animates them back when zooming out.\n\n" +
                        "With our sdk, You can style your clusters however you want (they are regular markers " +
                        "on the map), we decided to stick to the classic: white background, black number"

        val objectEditingInfoHeader = "VECTOR OBJECT EDITING"

        val objectEditingInfoContainer =
                "Now this is one of the more complex features of CARTOMobileSDK. The option to create " +
                        "custom elements, and then also to edit them!\n\n" +
                        "Simply click on the element you wish to edit, and then drag from the navy-colored " +
                        "dots to change its size or from the center to move it.\n\n" +
                        "We even installed a small trash can in the top right corner so you " +
                        "can delete and element if you get sick of it.\n\n"

        val gpsLocationInfoHeader = "GPS LOCATION"

        val gpsLocationInfoContainer =
                "CartoMobileSDK is also compatible with native APIs. You get latitude and longitude " +
                        "from CLLoationManager and you can easily place a highly customized marker on those coordinates.\n\n" +
                        "This example also has a 'track location' switch, as it by default zooms " +
                        "to your location when it receives a location update. Turn it off if you wish to browse the map"

        val groundOverlayInfoHeader = "GROUND OVERLAY"

        val groundOverlayInfoContainer =
                "CartoMobileSDK includes support for aligning raster overlay at geographically precise locations. " +
                        "This sample uses image of Thomas Jefferson Building and places it using coordinates of two corners.\n\n" +
                        "The sample uses RasterTileLayer with BitmapOverlayRasterTileDataSource class that provides high-quality " +
                        "image resampling for all zoom levels"

        val geocodingInfoHeader = "GEOCODING"

        val geocodingInfoContainer =
                "CartoMobileSDK version 4.1.0 introduces geocoding. You can now type in an " +
                        "adress and locate it on the map. And everything's offline!\n\n" +
                        "All you have to do is download the correct package (current sample features Estonia) " +
                        "and you're good to go\n\n" + "I don't really know what to say here any more. " +
                        "Geocoding is rather self-explanatory. It's an awesome feature, though, that's worth a mention!"

        val reverseGeocodingInfoHeader = "REVERSE GEOCODING"

        val reverseGeocodingInfoContainer =
                "CartoMobileSDK version 4.1.0 introduces (reverse) geocoding. " +
                        "You simply click an area on the map, any location, to find out detailed information about it." +
                        " And everything's offline!\n\n" +
                        "All you have to do is download the correct package (current sample features Estonia) and you're good to go\n\n" +
                        "I don't really know what to say here any more. Geocoding is rather self-explanatory. " +
                        "It's an awesome feature, though, that's worth a mention!"

        val offlineRoutingInfoHeader = "COUNTRY ROUTE DOWNLOAD"

        val offlineRoutingInfoContainer =
        "This example lets you download pre-defined packages. The packages are mostly country-based, but some larger countries, " +
                "like The United States, Russia and Germany, are municipality (or oblast etc.) based.\n\n" +
        "Simply choose a package, press download and it will start, the progress will be displayed in the list and on the map.\n\n" +
        "Our SDK also offers the option to pause, resume he download and, when you have it, the option to delete the package.\n\n" +
        "The packages are defined by CARTO's Mobile team and are readily available in our mobile SDK's API."
    }

}