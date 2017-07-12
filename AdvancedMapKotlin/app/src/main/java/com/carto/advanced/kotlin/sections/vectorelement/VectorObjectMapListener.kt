package com.carto.advanced.kotlin.sections.vectorelement

import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener

/**
 * Created by aareundo on 12/07/2017.
 */
class VectorObjectMapListener(val objectListener: VectorObjectClickListener) : MapEventListener() {

    override fun onMapClicked(mapClickInfo: MapClickInfo?) {
        objectListener.reset()
    }
}