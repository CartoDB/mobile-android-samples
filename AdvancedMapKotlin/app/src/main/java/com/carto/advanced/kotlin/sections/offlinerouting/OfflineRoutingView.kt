package com.carto.advanced.kotlin.sections.offlinerouting

import android.content.Context
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.PackageDownloadBaseView

/**
 * Created by aareundo on 18/08/2017.
 */
class OfflineRoutingView(context: Context) : PackageDownloadBaseView(context) {

    init {

        title = Texts.offlineRoutingInfoHeader
        description = Texts.offlineRoutingInfoContainer

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}