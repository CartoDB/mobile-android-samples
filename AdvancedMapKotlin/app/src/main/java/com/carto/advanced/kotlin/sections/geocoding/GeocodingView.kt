package com.carto.advanced.kotlin.sections.geocoding

import android.content.Context
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 11/07/2017.
 */
class GeocodingView(context: Context) : MapBaseView(context) {

    init {

        title = Texts.geocodingInfoHeader
        description = Texts.geocodingInfoContainer

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARK)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}