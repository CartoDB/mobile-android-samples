package com.carto.advanced.kotlin.sections.citydownload

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.BaseActivity

class CityDownloadActivity : BaseActivity() {

    var contentView: CityDownloadView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = CityDownloadView(this)
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
