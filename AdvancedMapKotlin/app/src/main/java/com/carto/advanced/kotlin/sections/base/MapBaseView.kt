package com.carto.advanced.kotlin.sections.base

import android.content.Context
import android.widget.RelativeLayout
import com.carto.ui.MapView

/**
 * Created by aareundo on 30/06/2017.
 */

class MapBaseView(context: Context) : BaseView(context) {

    var map: MapView = MapView(context)

    init {
        addView(map)
    }

    override fun layoutSubviews() {
        map.setFrame(0, 0, frame.width, frame.height)
    }
}