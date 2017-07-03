package com.carto.advanced.kotlin.sections.base.routedownload

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.base.BaseActivity

class RouteDownloadActivity : BaseActivity() {

    var contentView: RouteDownloadView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = RouteDownloadView(this)
        setContentView(contentView)
    }
}
