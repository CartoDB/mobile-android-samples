package com.carto.advanced.kotlin.sections.vectorelement

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.BaseActivity

class VectorElementActivity : BaseActivity() {

    var contentView: VectorElementView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = VectorElementView(this)
        setContentView(contentView)
    }
}
