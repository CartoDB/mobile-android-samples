package com.carto.advanced.kotlin.sections.editing

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.BaseActivity
import com.carto.layers.VectorEditEventListener

class EditingActivity : BaseActivity() {

    var contentView: EditingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = EditingView(this)
        setContentView(contentView)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.addElements()

    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()
    }
}
