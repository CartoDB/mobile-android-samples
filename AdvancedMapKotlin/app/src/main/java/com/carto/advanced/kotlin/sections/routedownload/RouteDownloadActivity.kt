package com.carto.advanced.kotlin.sections.routedownload

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.BaseActivity

class RouteDownloadActivity : BaseActivity() {

    var contentView: RouteDownloadView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = RouteDownloadView(this)
        setContentView(contentView)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()
    }
}
