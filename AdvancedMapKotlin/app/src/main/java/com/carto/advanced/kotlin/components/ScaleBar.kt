package com.carto.advanced.kotlin.components

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.utils.CGRect
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.core.ScreenPos
import com.carto.projections.Projection
import com.carto.ui.MapView
import java.lang.Math.*

/**
 * Basic ScaleBar implementation.
 *
 * Should be used together with MapEventListener (RotationListener in our case)
 * The calculation should be done on a different thread
 */
class ScaleBar(context: Context) : BaseView(context) {
    /**
     * MapView must be initialized for the calculation to work
     */
    var map: MapView? = null

    val projection: Projection
        get() = map!!.options!!.baseProjection

    val bottomBorder = BaseView(context)
    val leftBorder = BaseView(context)
    val rightBorder = BaseView(context)

    val label = TextView(context)

    init {

        val color = Color.GRAY

        label.gravity = Gravity.END ; Gravity.CENTER_VERTICAL
        label.setTextColor(color)
        label.textSize = 12.0f
        addView(label)

        bottomBorder.setBackgroundColor(color)
        addView(bottomBorder)

        leftBorder.setBackgroundColor(color)
        addView(leftBorder)

        rightBorder.setBackgroundColor(color)
        addView(rightBorder)
    }

    override fun layoutSubviews() {

        val padding = (5 * getDensity()).toInt()

        var x = padding
        var y = 0
        var w = frame.width - 2 * padding
        var h = frame.height - padding

        label.setFrame(x, y, w, h)

        w = frame.width
        h = (1 * getDensity()).toInt()
        x = 0
        y = frame.height - (h + padding)

        bottomBorder.setFrame(x, y, w, h)

        h = (6 * getDensity()).toInt()
        w = (1 * getDensity()).toInt()
        y = frame.height - (h + padding)

        leftBorder.setFrame(x, y, w, h)

        x = frame.width - w

        rightBorder.setFrame(x, y, w, h)
    }

    fun update() {

        if (map == null) {
            throw Exception("MapView must be initialized")
        }

        if (map!!.zoom < 5) {
            // Calculating scale is pointless if we're that far up
            return
        }

        val mapHeight = map!!.layoutParams.height
        val mapWidth = map!!.layoutParams.width

        // Take 2 map positions, left and right in middle of mapview
        val sp1 = ScreenPos(0.0f, mapHeight / 2.0f)
        val sp2 = ScreenPos(mapWidth.toFloat(), mapHeight / 2.0f)

        val pos1Wgs = projection.toWgs84(map!!.screenToMap(sp1))
        val pos2Wgs = projection.toWgs84(map!!.screenToMap(sp2))

        // Calculate distance with haversine formula
        // https://en.wikipedia.org/wiki/Haversine_formula

        val latDistance = (pos1Wgs.y - pos2Wgs.y).toRadians()
        val lonDistance = (pos1Wgs.x - pos2Wgs.x).toRadians()

        val AVERAGE_RADIUS_OF_EARTH = 6378137.0f

        val a =
                sin(latDistance / 2.0) * sin(latDistance / 2.0) +
                        cos(pos1Wgs.y.toRadians()) * cos(pos2Wgs.y.toRadians()) *
                                sin(lonDistance / 2.0) * sin(lonDistance / 2.0)

        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))

        // Total distance shown by the MapView
        val distanceMeters = AVERAGE_RADIUS_OF_EARTH * c

        val ratio = frame.width.toFloat() / mapWidth.toFloat()
        val scale = distanceMeters * ratio

        (context as Activity).runOnUiThread {
            if (scale > 1000) {
                label.text = (scale / 1000).toInt().toString() + "km"
            } else {
                label.text = scale.toInt().toString() + "m"
            }
        }
    }
}
