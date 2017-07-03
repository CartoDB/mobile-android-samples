package com.carto.advanced.kotlin.sections.base.citydownload

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.base.BaseActivity

class CityDownloadActivity : BaseActivity() {

    var contentView: CityDownloadView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = CityDownloadView(this)
        setContentView(contentView)
    }
}
