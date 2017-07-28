package com.carto.advanced.kotlin.sections.gpslocation

import android.content.Context
import android.location.Location
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.RotationResetButton
import com.carto.advanced.kotlin.components.SwitchButton
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.CGRect
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.advanced.kotlin.sections.base.toCartoColor
import com.carto.advanced.kotlin.utils.Colors
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.core.MapPosVectorVector
import com.carto.datasources.LocalVectorDataSource
import com.carto.geometry.PolygonGeometry
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.VectorLayer
import com.carto.styles.LineStyleBuilder
import com.carto.styles.PointStyleBuilder
import com.carto.styles.PolygonStyleBuilder
import com.carto.vectorelements.Point
import com.carto.vectorelements.Polygon

/**
 * Created by aareundo on 03/07/2017.
 */
class GPSLocationView(context: Context) : MapBaseView(context) {

    var switch = SwitchButton(context, R.drawable.icon_track_location_on, R.drawable.icon_track_location_off)

    var source: LocalVectorDataSource? = null

    var rotationResetButton = RotationResetButton(context)

    init {

        title = Texts.gpsLocationInfoHeader
        description = Texts.gpsLocationInfoContainer

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY)

        addButton(switch)

        source = LocalVectorDataSource(projection)
        val layer = VectorLayer(source)
        map.layers.add(layer)

        addView(rotationResetButton)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding: Int = (10 * getDensity()).toInt()

        val w = buttonSize - padding
        val h = w
        val x: Int = frame.width - (w + padding)
        val y = padding

        rotationResetButton.setFrame(x, y, w, h)
    }

    var userMarker: Point? = null
    var accuracyMarker: Polygon? = null

    fun showUserAt(location: Location) {

        val latitude = location.latitude
        val longitude = location.longitude
        val accuracy = location.accuracy

        val position = projection?.fromWgs84(MapPos(longitude, latitude))
        map.setFocusPos(position, 1.0f)
        map.setZoom(16.0f, 1.0f)

        val builder = PolygonStyleBuilder()
        builder.color = Colors.lightTransparentAppleBlue.toCartoColor()
        val borderBuilder = LineStyleBuilder()
        borderBuilder.color = Colors.darkTransparentAppleBlue.toCartoColor()
        borderBuilder.width = 1.0f
        builder.lineStyle = borderBuilder.buildStyle()

        val points = getCirclePoints(latitude, longitude, accuracy)

        if (accuracyMarker == null) {
            accuracyMarker = Polygon(points, MapPosVectorVector(), builder.buildStyle())
            source?.add(accuracyMarker)
        } else {
            accuracyMarker?.style = builder.buildStyle()
            accuracyMarker?.geometry = PolygonGeometry(points)
        }

        if (userMarker == null) {
            @Suppress("NAME_SHADOWING")
            val builder = PointStyleBuilder()
            builder.size = 15.0f
            builder.color = Colors.appleBlue.toCartoColor()
            userMarker = Point(position, builder.buildStyle())
            source?.add(userMarker)
        }

        userMarker?.pos = position
    }

    val N = 100
    val EARTH_RADIUS = 6378137.0

    fun getCirclePoints(latitude: Double, longitude: Double, accuracy: Float): MapPosVector {
        val points = MapPosVector()

        val radius = accuracy.toDouble()

        for (i in 0..N) {
            val angle = Math.PI * 2 * (i % N) / N
            val dx = radius * Math.cos(angle)
            val dy = radius * Math.sin(angle)

            val lat = latitude + (180 / Math.PI) * (dy / EARTH_RADIUS)
            val lon = longitude + (180 / Math.PI) * (dx / EARTH_RADIUS) / Math.cos(latitude * Math.PI / 180)

            val point = projection?.fromWgs84(MapPos(lon, lat))
            points.add(point)
        }

        return points
    }
}