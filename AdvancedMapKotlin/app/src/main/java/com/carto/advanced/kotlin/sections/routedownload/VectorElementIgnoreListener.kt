package com.carto.advanced.kotlin.sections.routedownload

import com.carto.layers.VectorElementEventListener
import com.carto.ui.VectorElementClickInfo

/**
 * Created by aareundo on 17/07/2017.
 */
class VectorElementIgnoreListener : VectorElementEventListener() {

    override fun onVectorElementClicked(clickInfo: VectorElementClickInfo?): Boolean {
        return false
    }
}