package com.carto.advanced.kotlin.sections.base.reversegeocoding

import android.content.Context
import com.carto.advanced.kotlin.sections.base.base.MapBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 11/07/2017.
 */
class ReverseGeocodingView(context: Context) : MapBaseView(context) {

    init {
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARK)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}