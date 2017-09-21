package com.carto.advanced.kotlin.sections.base.utils

/**
 * Created by aareundo on 30/06/2017.
 */

class CGRect(x: Int, y: Int, width: Int, height: Int) {

    var x: Int = 0
    var y: Int = 0
    var width: Int = 0
    var height: Int = 0

    init {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
    }

    fun getBounds(): com.carto.advanced.kotlin.sections.base.utils.CGRect {
        return com.carto.advanced.kotlin.sections.base.utils.CGRect(0, 0, width, height)
    }

    companion object {
        @JvmField val empty = com.carto.advanced.kotlin.sections.base.utils.CGRect(0, 0, 0, 0)
    }
}
