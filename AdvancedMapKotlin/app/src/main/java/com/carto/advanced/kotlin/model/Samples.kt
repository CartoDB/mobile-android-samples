package com.carto.advanced.kotlin.model

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.sections.base.citydownload.CityDownloadActivity
import com.carto.advanced.kotlin.sections.base.clustering.ClusteringActivity
import com.carto.advanced.kotlin.sections.base.editing.EditingActivity
import com.carto.advanced.kotlin.sections.base.gpslocation.GPSLocationActivity
import com.carto.advanced.kotlin.sections.base.packagedownload.PackageDownloadActivity
import com.carto.advanced.kotlin.sections.base.routedownload.RouteDownloadActivity
import com.carto.advanced.kotlin.sections.base.styles.StyleChoiceActivity
import com.carto.advanced.kotlin.sections.base.vectorelement.VectorElementActivity
import java.lang.reflect.Type

/**
 * Created by aareundo on 30/06/2017.
 */
class Samples {

    companion object {
        val list: MutableList<Sample> = mutableListOf(
                Sample(R.drawable.icon_sample_styles, "STYLES",
                        "Various samples of different CARTO Base Maps", StyleChoiceActivity::class.java
                ),
                Sample(R.drawable.icon_sample_route_download, "ROUTE DOWNLOAD",
                        "Route download via bounding box for offline use", RouteDownloadActivity::class.java
                ),
                Sample(R.drawable.icon_sample_city_download, "CITY DOWLOAD",
                        "City download via bounding box for offline use", CityDownloadActivity::class.java
                ),
                Sample(R.drawable.icon_sample_package_download, "PACKAGE DOWNLOAD",
                        "Download existing packages for offline use", PackageDownloadActivity::class.java
                ),
                Sample(R.drawable.icon_sample_vector_objects, "VECTOR ELEMENTS",
                        "Different popups, polygons and a NMLModel", VectorElementActivity::class.java
                ),
                Sample(R.drawable.icon_sample_clustering, "ELEMENT CLUSTERING",
                        "Loads 20000 elements and shows as clusters", ClusteringActivity::class.java
                ),
                Sample(R.drawable.icon_sample_object_editing, "OBJECT EDITING",
                        "Places editable objects on the world map", EditingActivity::class.java
                ),
                Sample(R.drawable.icon_sample_gps_location, "GPS LOCATION",
                        "Locates you and places a marker on the location", GPSLocationActivity::class.java
                )
        )
    }



}

class Sample() {

    var imageResource: Int? = null

    var title: String? = null

    var description: String? = null

    var activity: Class<*>? = null

    init {

    }

    constructor(imageResource: Int, title: String, description: String, activity: Class<*>) : this() {
        this.imageResource = imageResource
        this.title = title
        this.description = description
        this.activity = activity
    }
}