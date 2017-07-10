package com.carto.advanced.kotlin.sections.base.editing

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.base.BaseActivity

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
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()
    }
}
