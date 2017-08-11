package com.carto.advanced.kotlin.model

import com.carto.advanced.kotlin.utils.BoundingBox

/**
 * Created by aareundo on 13/07/2017.
 */
class City(val name: String, val bbox: BoundingBox) {

    val existsLocally
        get() = size != -1.0

    var size: Double = -1.0
}