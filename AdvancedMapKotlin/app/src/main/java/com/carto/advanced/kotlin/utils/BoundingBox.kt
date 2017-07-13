package com.carto.advanced.kotlin.utils

import com.carto.core.MapPos
import java.util.*


/**
 * Created by aareundo on 13/07/2017.
 */
class BoundingBox(var minLon: Double, var maxLon: Double, var minLat: Double, var maxLat: Double) {

    val center: MapPos
        get() = MapPos((maxLon + minLon) / 2, (maxLat + minLat) / 2)

    override fun toString(): String {
        return String.format(Locale.US, "bbox($minLon,$minLat,$maxLon,$maxLat)", null)
    }
}