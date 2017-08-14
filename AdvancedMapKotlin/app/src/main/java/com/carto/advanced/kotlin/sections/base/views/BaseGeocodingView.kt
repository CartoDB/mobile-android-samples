package com.carto.advanced.kotlin.sections.base.views

import com.carto.advanced.kotlin.utils.toList
import com.carto.styles.AnimationStyleBuilder
import com.carto.styles.AnimationType

/**
 * Created by aareundo on 20/07/2017.
 */
open class BaseGeocodingView(context: android.content.Context) : PackageDownloadBaseView(context) {

    companion object {
        val SOURCE = "geocoding:carto.streets"
    }

    val source = com.carto.datasources.LocalVectorDataSource(projection)

    init {

        val layer = com.carto.layers.VectorLayer(source)
        map.layers.add(layer)

        removeSwitch()
    }

    fun showResult(result: com.carto.geocoding.GeocodingResult, title: String, description: String, goToPosition: Boolean) {

        source.clear()

        val animationBuilder = AnimationStyleBuilder()
        animationBuilder.relativeSpeed = 2.0f
        animationBuilder.fadeAnimationType = AnimationType.ANIMATION_TYPE_SMOOTHSTEP

        val builder = com.carto.styles.BalloonPopupStyleBuilder()
        builder.leftMargins = com.carto.styles.BalloonPopupMargins(0, 0, 0, 0)
        builder.titleMargins = com.carto.styles.BalloonPopupMargins(6, 3, 6, 3)
        builder.cornerRadius = 5
        builder.animationStyle = animationBuilder.buildStyle()

        // Make sure this label is shown on top of all other labels
        builder.placementPriority = 10

        val collection = result.featureCollection
        val count = collection.featureCount

        var position: com.carto.core.MapPos? = null

        var geometry: com.carto.geometry.Geometry?

        for (i in 0..count - 1) {

            geometry = collection?.getFeature(i)!!.geometry
            val color = com.carto.graphics.Color(0, 100, 200, 150)

            // Build styles for the displayed geometry
            val pointBuilder = com.carto.styles.PointStyleBuilder()
            pointBuilder.color = color

            val lineBuilder = com.carto.styles.LineStyleBuilder()
            lineBuilder.color = color

            val polygonBuilder = com.carto.styles.PolygonStyleBuilder()
            polygonBuilder.color = color

            var element: com.carto.vectorelements.VectorElement? = null

            if (geometry is com.carto.geometry.PointGeometry) {
                element = com.carto.vectorelements.Point(geometry, pointBuilder.buildStyle())

            } else if (geometry is com.carto.geometry.LineGeometry) {
                element = com.carto.vectorelements.Line(geometry, lineBuilder.buildStyle())
            } else if (geometry is com.carto.geometry.PolygonGeometry) {
                element = com.carto.vectorelements.Polygon(geometry, polygonBuilder.buildStyle())

            } else if (geometry is com.carto.geometry.MultiGeometry) {

                val collectionBuilder = com.carto.styles.GeometryCollectionStyleBuilder()
                collectionBuilder.pointStyle = pointBuilder.buildStyle()
                collectionBuilder.lineStyle = lineBuilder.buildStyle()
                collectionBuilder.polygonStyle = polygonBuilder.buildStyle()

                element = com.carto.vectorelements.GeometryCollection(geometry, collectionBuilder.buildStyle())
            }

            position = geometry?.centerPos
            source.add(element)
        }

        if (goToPosition) {

            map.setFocusPos(position, 1.0f)
            map.setZoom(17.0f, 1.0f)
        }

        val popup = com.carto.vectorelements.BalloonPopup(position, builder.buildStyle(), title, description)
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

    fun getLocalPackages(): MutableList<com.carto.packagemanager.PackageInfo> {
        return manager!!.localPackages.toList()
    }
}