package com.carto.advanced.kotlin.sections.packagedownload

import android.content.Context
import com.carto.advanced.kotlin.sections.base.DownloadBaseView
import com.carto.advanced.kotlin.sections.base.PackageDownloadBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 03/07/2017.
 */
class PackageDownloadView(context: Context) : PackageDownloadBaseView(context) {

    init {
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}