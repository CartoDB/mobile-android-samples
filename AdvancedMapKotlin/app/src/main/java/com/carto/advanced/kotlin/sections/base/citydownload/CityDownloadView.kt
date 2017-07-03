package com.carto.advanced.kotlin.sections.base.citydownload

import android.content.Context
import com.carto.advanced.kotlin.sections.base.base.DownloadBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 03/07/2017.
 */
class CityDownloadView(context: Context) : DownloadBaseView(context) {

    init {
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}