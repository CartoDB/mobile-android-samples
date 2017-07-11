package com.carto.advanced.kotlin.sections.base.geocoding

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.base.BaseActivity

/**
 * Created by aareundo on 11/07/2017.
 */
class GeocodingActivity : BaseActivity() {

    var contentView: GeocodingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = GeocodingView(this)
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