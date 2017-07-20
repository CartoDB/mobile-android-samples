package com.carto.advanced.kotlin.sections.reversegeocoding

import android.content.Context
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.BaseGeocodingView
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 11/07/2017.
 */
class ReverseGeocodingView(context: Context) : BaseGeocodingView(context) {

    init {

        title = Texts.reverseGeocodingInfoHeader
        description = Texts.reverseGeocodingInfoContainer

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}