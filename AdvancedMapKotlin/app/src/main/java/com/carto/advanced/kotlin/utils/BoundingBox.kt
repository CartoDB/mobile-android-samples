package com.carto.advanced.kotlin.utils

import com.carto.core.MapBounds
import com.carto.core.MapPos
import com.carto.projections.Projection
import java.util.*


/**
 * Created by aareundo on 13/07/2017.
 */
class BoundingBox(var minLon: Double, var maxLon: Double, var minLat: Double, var maxLat: Double) {

    companion object {
        val identifier = "bbox("

        fun fromString(projection: Projection, string: String): BoundingBox {

            val split = string.split(",")

            val minLon = split[0].replace(identifier, "").toDouble()
            val minLat = split[1].toDouble()
            val maxLon = split[2].toDouble()
            val maxLat = split[3].replace(")", "").toDouble()

            val box = BoundingBox(minLon, maxLon, minLat, maxLat)

            val min = projection.fromLatLong(minLat, minLon)
            val max = projection.fromLatLong(maxLat, maxLon)
            box.bounds = MapBounds(min, max)

            return box
        }

        fun fromMapBounds(projection: Projection, bounds: MapBounds): BoundingBox {

            val min = MapPos(bounds.min.x, bounds.min.y)
            val max = MapPos(bounds.max.x, bounds.max.y)

            val minWgs = projection.toWgs84(min)
            val maxWgs = projection.toWgs84(max)

            return BoundingBox(minWgs.x, maxWgs.x, minWgs.y, maxWgs.y)
        }
    }

    var bounds: MapBounds? = null

    val center: MapPos
        get() = MapPos((maxLon + minLon) / 2, (maxLat + minLat) / 2)

    override fun toString(): String {
        return String.format(Locale.US, "bbox($minLon,$minLat,$maxLon,$maxLat)", null)
    }
}