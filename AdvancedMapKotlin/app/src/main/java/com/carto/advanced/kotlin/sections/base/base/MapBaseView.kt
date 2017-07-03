package com.carto.advanced.kotlin.sections.base.base

import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.projections.Projection

/**
 * Created by aareundo on 30/06/2017.
 */

open class MapBaseView(context: android.content.Context) : BaseView(context) {

    var map: com.carto.ui.MapView = com.carto.ui.MapView(context)

    var projection: Projection? = null

    init {

        projection = map.options.baseProjection

        addView(map)

        setMainViewFrame()
    }

    override fun layoutSubviews() {
        map.setFrame(0, 0, frame.width, frame.height)
    }

    fun addBaseLayer(style: CartoBaseMapStyle): CartoOnlineVectorTileLayer {

        val layer = CartoOnlineVectorTileLayer(style)
        map.layers.add(layer)
        return layer
    }
}