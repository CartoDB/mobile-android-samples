package com.carto.advanced.kotlin.sections.clustering

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.BaseActivity

class ClusteringActivity : BaseActivity() {

    var contentView: ClusteringView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = ClusteringView(this)
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
