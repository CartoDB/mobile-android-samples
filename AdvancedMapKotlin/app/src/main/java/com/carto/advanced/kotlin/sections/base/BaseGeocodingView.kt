package com.carto.advanced.kotlin.sections.base

import android.content.Context
import com.carto.core.MapPos
import com.carto.core.ScreenBounds
import com.carto.core.ScreenPos
import com.carto.datasources.LocalVectorDataSource
import com.carto.geocoding.GeocodingResult
import com.carto.geometry.*
import com.carto.graphics.Color
import com.carto.layers.VectorLayer
import com.carto.styles.*
import com.carto.vectorelements.*

/**
 * Created by aareundo on 20/07/2017.
 */
open class BaseGeocodingView(context: Context) : PackageDownloadBaseView(context) {

    companion object {
        val SOURCE = "geocoding:carto.geocode"
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

        var geometry: Geometry? = null

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

            val min = ScreenPos(10.0f, 10.0f)
            val max = ScreenPos(map.layoutParams.width - 20.0f, map.layoutParams.height - 20.0f)
            val bounds = ScreenBounds(min, max)

            map.moveToFitBounds(geometry?.bounds, bounds, false, 0.5f)
        }

        val popup = BalloonPopup(position, builder.buildStyle(), title, description)
        source.add(popup)
    }
}