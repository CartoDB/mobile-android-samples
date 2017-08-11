package com.carto.advanced.kotlin.sections.groundoverlay

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity

/**
 * Created by mark on 10/08/2017.
 */
class GroundOverlayActivity : BaseActivity() {

    var contentView: GroundOverlayView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = GroundOverlayView(this)
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
