package com.carto.advanced.kotlin.sections.base.styles

import android.content.Context
import com.carto.advanced.kotlin.sections.base.base.MapBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 03/07/2017.
 */
class StyleChoiceView(context: Context) : MapBaseView(context) {

    init {
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}