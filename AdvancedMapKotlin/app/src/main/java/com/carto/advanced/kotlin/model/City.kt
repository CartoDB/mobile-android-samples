package com.carto.advanced.kotlin.model

import com.carto.advanced.kotlin.utils.BoundingBox

/**
 * Created by aareundo on 13/07/2017.
 */
class City(val name: String, val bbox: BoundingBox) {

    var existsLocally = false
}