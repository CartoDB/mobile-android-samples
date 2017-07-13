package com.carto.advanced.kotlin.sections.editing

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.carto.R
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.advanced.kotlin.sections.base.toCartoColor
import com.carto.advanced.kotlin.utils.Colors
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.layers.EditableVectorLayer
import com.carto.styles.LineStyleBuilder
import com.carto.styles.PointStyleBuilder
import com.carto.styles.PolygonStyleBuilder
import com.carto.vectorelements.Line
import com.carto.vectorelements.Point
import com.carto.vectorelements.Polygon

/**
 * Created by aareundo on 03/07/2017.
 */
class EditingView(context: Context) : MapBaseView(context) {

    var baseLayer: CartoOnlineVectorTileLayer? = null

    var editLayer: EditableVectorLayer? = null

    var editSource: LocalVectorDataSource? = null

    var trashCan = ImageView(context)

    init {

        title = Texts.objectEditingInfoHeader
        description = Texts.objectEditingInfoContainer

        baseLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARK)

        editSource = LocalVectorDataSource(projection)
        editLayer = EditableVectorLayer(editSource)
        map.layers.add(editLayer)

        topBanner.setText("CLICK ON AN ELEMENT TO EDIT IT")

        trashCan.setImageResource(com.carto.advanced.kotlin.R.drawable.icon_trashcan)
        trashCan.scaleType = ImageView.ScaleType.CENTER_CROP
        topBanner.setRightItem(trashCan)
        trashCan.visibility = View.GONE

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    fun addElements() {

        editSource?.clear()

        val color = Colors.green.toCartoColor()
        val positions = MapPosVector()

        /*
         * Points that form a circle of lines, the contours of the face
         */
        val lineBuilder = LineStyleBuilder()
        lineBuilder.color = color

        val minY: Double = -80.0
        val maxY: Double = Math.abs(minY)

        val minX: Double = -170.0
        val maxX: Double = Math.abs(minX)

        // Counter-clockwise circle starting from south-west/bottom-left
        positions.add(projection?.fromWgs84(MapPos(-30.0, minY)))
        positions.add(projection?.fromWgs84(MapPos(-110.0, -75.0)))
        positions.add(projection?.fromWgs84(MapPos(minX, -40.0)))
        positions.add(projection?.fromWgs84(MapPos(minX, 40.0)))
        positions.add(projection?.fromWgs84(MapPos(-110.0, 75.0)))
        positions.add(projection?.fromWgs84(MapPos(-30.0, maxY)))
        positions.add(projection?.fromWgs84(MapPos(30.0, maxY)))
        positions.add(projection?.fromWgs84(MapPos(110.0, 75.0)))
        positions.add(projection?.fromWgs84(MapPos(maxX, 40.0)))
        positions.add(projection?.fromWgs84(MapPos(maxX, -40.0)))
        positions.add(projection?.fromWgs84(MapPos(110.0, -75.0)))
        positions.add(projection?.fromWgs84(MapPos(30.0, minY)))
        positions.add(projection?.fromWgs84(MapPos(-30.0, minY)))

        var line = Line(positions, lineBuilder.buildStyle())
        editSource?.add(line)

        positions.clear()

        /*
         * Points, eyes
         */
        var position = projection?.fromWgs84(MapPos(-50.0, 50.0))
        val pointBuilder = PointStyleBuilder()
        pointBuilder.color = color

        var point = Point(position, pointBuilder.buildStyle())
        editSource?.add(point)

        position = projection?.fromWgs84(MapPos(50.0, 50.0))
        point = Point(position, pointBuilder.buildStyle())
        editSource?.add(point)

        /*
         * Polygon, nose
         */
        positions.add(projection?.fromWgs84(MapPos(0.0, 20.0)))
        positions.add(projection?.fromWgs84(MapPos(-35.0, -30.0)))
        positions.add(projection?.fromWgs84(MapPos(0.0, -30.0)))

        val polygonBuilder = PolygonStyleBuilder()
        polygonBuilder.color = color

        val polygon = Polygon(positions, polygonBuilder.buildStyle())
        editSource?.add(polygon)

        positions.clear()

        /*
         * Lines, mouth
         */
        positions.add(projection?.fromWgs84(MapPos(0.0, -65.0)))
        positions.add(projection?.fromWgs84(MapPos(60.0, -65.0)))
        positions.add(projection?.fromWgs84(MapPos(90.0, -55.0)))
        positions.add(projection?.fromWgs84(MapPos(100.0, -45.0)))
        positions.add(projection?.fromWgs84(MapPos(110.0, -20.0)))

        line = Line(positions, lineBuilder.buildStyle())
        editSource?.add(line)
    }
}










