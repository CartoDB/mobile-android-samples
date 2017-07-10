package com.carto.advanced.kotlin.sections.base.packagedownload

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.base.BaseActivity

class PackageDownloadActivity : BaseActivity() {

    var contentView: PackageDownloadView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = PackageDownloadView(this)
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
