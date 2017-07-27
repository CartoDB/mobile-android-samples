package com.carto.advanced.kotlin.sections.base

import android.content.Context
import com.carto.advanced.kotlin.utils.toList
import com.carto.core.MapPos
import com.carto.datasources.LocalVectorDataSource
import com.carto.geocoding.GeocodingResult
import com.carto.geometry.*
import com.carto.graphics.Color
import com.carto.layers.VectorLayer
import com.carto.packagemanager.PackageInfo
import com.carto.styles.*
import com.carto.vectorelements.*

/**
 * Created by aareundo on 20/07/2017.
 */
open class BaseGeocodingView(context: Context) : PackageDownloadBaseView(context) {

    companion object {
        val SOURCE = "geocoding:carto.streets"
    }

    val source = LocalVectorDataSource(projection)

    init {

        val layer = VectorLayer(source)
        map.layers.add(layer)
    }

    fun showResult(result: GeocodingResult, title: String, description: String, goToPosition: Boolean) {

        source.clear()

        val builder = BalloonPopupStyleBuilder()
        builder.leftMargins = BalloonPopupMargins(0, 0, 0, 0)
        builder.titleMargins = BalloonPopupMargins(6, 3, 6, 3)
        builder.cornerRadius = 5

        // Make sure this label is shown on top of all other labels
        builder.placementPriority = 10

        val collection = result.featureCollection
        val count = collection.featureCount

        var position: MapPos? = null

        var geometry: Geometry?

        for (i in 0..count - 1) {

            geometry = collection?.getFeature(i)!!.geometry
            val color = Color(0, 100, 200, 150)

            // Build styles for the displayed geometry
            val pointBuilder = PointStyleBuilder()
            pointBuilder.color = color

            val lineBuilder = LineStyleBuilder()
            lineBuilder.color = color

            val polygonBuilder = PolygonStyleBuilder()
            polygonBuilder.color = color

            var element: VectorElement? = null

            if (geometry is PointGeometry) {
                element = Point(geometry, pointBuilder.buildStyle())

            } else if (geometry is LineGeometry) {
                element = Line(geometry, lineBuilder.buildStyle())
            } else if (geometry is PolygonGeometry) {
                element = Polygon(geometry, polygonBuilder.buildStyle())

            } else if (geometry is MultiGeometry) {

                val collectionBuilder = GeometryCollectionStyleBuilder()
                collectionBuilder.pointStyle = pointBuilder.buildStyle()
                collectionBuilder.lineStyle = lineBuilder.buildStyle()
                collectionBuilder.polygonStyle = polygonBuilder.buildStyle()

                element = GeometryCollection(geometry, collectionBuilder.buildStyle())
            }

            position = geometry?.centerPos
            source.add(element)
        }

        if (goToPosition) {

            map.setFocusPos(position, 1.0f)
            map.setZoom(17.0f, 1.0f)
        }

        val popup = BalloonPopup(position, builder.buildStyle(), title, description)
        source.add(popup)
    }

    fun showLocalPackages() {
        var text = "You have downloaded "

        val packages = getLocalPackages()
        val total = packages.size
        var counter = 0

        for (item in packages) {
            val split = item.name.split("/")
            val shortName = split[split.size - 1]

            text += shortName
            counter++

            if (counter < total) {
                text += ", "
            }
        }

        progressLabel.complete(text)
    }

    fun hasLocalPackages(): Boolean {
        return getLocalPackages().size > 0
    }

    fun getLocalPackages(): MutableList<PackageInfo> {
        return manager!!.localPackages.toList()
    }
}