package com.carto.advanced.kotlin.sections.styles

import android.content.Context
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 03/07/2017.
 */
class StyleChoiceView(context: Context) : MapBaseView(context) {

    init {

        title = Texts.basemapInfoHeader
        description = Texts.basemapInfoContainer

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}