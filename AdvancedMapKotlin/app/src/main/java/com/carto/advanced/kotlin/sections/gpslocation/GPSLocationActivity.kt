package com.carto.advanced.kotlin.sections.gpslocation

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.BaseActivity

class GPSLocationActivity : BaseActivity() {

    var contentView: GPSLocationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = GPSLocationView(this)
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
