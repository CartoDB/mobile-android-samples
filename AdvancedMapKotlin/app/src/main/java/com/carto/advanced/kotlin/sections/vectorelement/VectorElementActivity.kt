package com.carto.advanced.kotlin.sections.vectorelement

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.BaseActivity

class VectorElementActivity : BaseActivity() {

    var contentView: VectorElementView? = null

    var mapListener: VectorObjectMapListener? = null
    var objectListener: VectorObjectClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = VectorElementView(this)
        setContentView(contentView)


        objectListener = VectorObjectClickListener(contentView?.source!!)
        mapListener = VectorObjectMapListener(objectListener!!)
    }

    override fun onResume() {
        super.onResume()

        contentView?.addListeners()

        contentView?.objectLayer?.vectorElementEventListener = objectListener
        contentView?.map?.mapEventListener = mapListener
    }

    override fun onPause() {
        super.onPause()

        contentView?.removeListeners()

        contentView?.objectLayer?.vectorElementEventListener = null
        contentView?.map?.mapEventListener = null
    }
}
