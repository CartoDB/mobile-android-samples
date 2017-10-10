package com.carto.advanced.kotlin.sections.styles

import com.carto.datasources.LocalVectorDataSource
import com.carto.geometry.LineGeometry
import com.carto.geometry.MultiGeometry
import com.carto.geometry.PointGeometry
import com.carto.geometry.PolygonGeometry
import com.carto.graphics.Color
import com.carto.layers.VectorLayer
import com.carto.layers.VectorTileEventListener
import com.carto.styles.*
import com.carto.ui.VectorTileClickInfo
import com.carto.vectorelements.*

/**
 * Created by aareundo on 10/10/2017.
 */
class VectorTileListener(private var layer: VectorLayer) : VectorTileEventListener() {

    override fun onVectorTileClicked(clickInfo: VectorTileClickInfo): Boolean {
        val source = layer.dataSource as LocalVectorDataSource

        source.clear()

        val color = Color(0.toShort(), 100.toShort(), 200.toShort(), 150.toShort())

        val feature = clickInfo.feature
        val geometry = feature.geometry

        val pointBuilder = PointStyleBuilder()
        pointBuilder.color = color

        val lineBuilder = LineStyleBuilder()
        lineBuilder.color = color

        val polygonBuilder = PolygonStyleBuilder()
        polygonBuilder.color = color

        if (geometry is PointGeometry) {
            source.add(Point(geometry, pointBuilder.buildStyle()))
        } else if (geometry is LineGeometry) {
            source.add(Line(geometry, lineBuilder.buildStyle()))
        } else if (geometry is PolygonGeometry) {
            source.add(Polygon(geometry, polygonBuilder.buildStyle()))
        } else if (geometry is MultiGeometry) {

            val collectionBuilder = GeometryCollectionStyleBuilder()
            collectionBuilder.pointStyle = pointBuilder.buildStyle()
            collectionBuilder.lineStyle = lineBuilder.buildStyle()
            collectionBuilder.polygonStyle = polygonBuilder.buildStyle()

            source.add(GeometryCollection(geometry, collectionBuilder.buildStyle()))
        }

        val builder = BalloonPopupStyleBuilder()

        // Set a higher placement priority so it would always be visible
        builder.placementPriority = 10

        val message = feature.properties.toString()

        val popup = BalloonPopup(clickInfo.clickPos, builder.buildStyle(), "Click", message)

        source.add(popup)

        return true
    }
}