package com.carto.advanced.kotlin.routing

import com.carto.core.MapBounds

/**
 * Created by aareundo on 17/07/2017.
 */
class Route {

    var length = 0.0

    var bounds = MapBounds()

    fun getLengthString(): String {
        return (length / 1000).toString() + "km"
    }
}