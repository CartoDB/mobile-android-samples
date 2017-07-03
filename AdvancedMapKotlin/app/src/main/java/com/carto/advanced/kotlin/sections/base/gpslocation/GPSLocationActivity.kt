package com.carto.advanced.kotlin.sections.base.gpslocation

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.base.BaseActivity

class GPSLocationActivity : BaseActivity() {

    var contentView: GPSLocationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = GPSLocationView(this)
        setContentView(contentView)
    }
}
