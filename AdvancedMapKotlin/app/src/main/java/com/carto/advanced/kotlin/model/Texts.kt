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
                        "just for visualizations that work better with a dark background. \n \n" +
                        "Mapzen and CARTO are similar in that way, " +
                        "the difference lies in the fact that CARTO's base maps use raster tiles, " +
                        "and mapzen's styles come from a different source."

        val routeDownloadInfoHeader = "ROUTE DOWNLOAD"

        val routeDownloadInfoContainer =
                "CARTOMobileSDK 4.1.0 uses Valhalla routing. With that, we have the option to " +
                        "download a specific bounding box... and that is exactly what this example does.\n\n" +
                        "It first downloads the map package, then saves it to a specified folder, " +
                        "then downloads the routing package separately. " +
                        "Fun fact: routing packages are often even larger than map packages.\n\n" +
                        "Long-click anywhere on the map, that's the start position " +
                        "– now click again to set the stop position. When you're done, the route is calculated.\n\n" +
                        "Now you have the option to download that bounding box. After you download a route," +
                        " a transparent visualization of the downloaded area appears on your map.\n\n" +
                        "Oh, and downloaded areas stay forever. You're gonna have to uninstall the app to get rid " +
                        "of the bounding boxes. So be careful, don't fill your phone with bounding boxes!\n\n"

        val cityDownloadInfoHeader = "CITY DOWNLOAD"

        val cityDownloadInfoContainer =
                "This example lets you download specific languages. Cities are also based on bounding boxes, " +
                        "we used http://bboxfinder.com to cut out the bounding boxes of specific languages.\n\n" +
                        "Simply click on the item you wish to download, a progress bar will appear " +
                        "and your're free to browse the map as you wish. " +
                        "You will be zoomed in to the location when the download is completed.\n\n" +
                        "If you wish to see more languages on this list, simply contact CARTO or, if you're " +
                        "the technical kind, find this app on github.com and make a pull request."

        val packageDownloadInfoHeader = "PACKAGE DOWNLOAD"

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
    }

}