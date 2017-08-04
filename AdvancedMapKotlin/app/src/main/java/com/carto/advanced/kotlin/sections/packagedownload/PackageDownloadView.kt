package com.carto.advanced.kotlin.sections.packagedownload

import android.content.Context
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.PackageDownloadBaseView

/**
 * Created by aareundo on 03/07/2017.
 */
class PackageDownloadView(context: Context) : PackageDownloadBaseView(context) {

    init {

        title = Texts.packageDownloadInfoHeader
        description = Texts.packageDownloadInfoContainer

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}