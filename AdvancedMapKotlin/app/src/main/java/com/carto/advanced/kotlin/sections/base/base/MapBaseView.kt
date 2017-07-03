package com.carto.advanced.kotlin.sections.base.base

/**
 * Created by aareundo on 30/06/2017.
 */

open class MapBaseView(context: android.content.Context) : BaseView(context) {

    var map: com.carto.ui.MapView = com.carto.ui.MapView(context)

    init {
        addView(map)
    }

    override fun layoutSubviews() {
        map.setFrame(0, 0, frame.width, frame.height)
    }
}