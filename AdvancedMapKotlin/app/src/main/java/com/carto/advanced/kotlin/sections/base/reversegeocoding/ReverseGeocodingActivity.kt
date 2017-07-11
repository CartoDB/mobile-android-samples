package com.carto.advanced.kotlin.sections.base.reversegeocoding

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.base.BaseActivity

/**
 * Created by aareundo on 11/07/2017.
 */
class ReverseGeocodingActivity : BaseActivity() {

    var contentView: ReverseGeocodingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = ReverseGeocodingView(this)
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