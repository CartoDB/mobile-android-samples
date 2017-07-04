package com.carto.advanced.kotlin.sections.base.base

import android.content.Context
import com.carto.advanced.kotlin.MapApplication
import com.carto.advanced.kotlin.components.ProgressLabel
import com.carto.advanced.kotlin.components.StateSwitch

/**
 * Created by aareundo on 03/07/2017.
 */
open class DownloadBaseView(context: Context) : MapBaseView(context) {

    val onlineSwitch = StateSwitch(context)
    val progressLabel = ProgressLabel(context)

    init {

        addView(onlineSwitch)
        addView(progressLabel)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding: Int = 5

        var w: Int = onlineSwitch.getTotalWidth()
        var h: Int = onlineSwitch.getTotalHeight()
        var y: Int = padding
        var x: Int = frame.width - (w + padding)

        onlineSwitch.setFrame(x, y, w, h)

        // I have no idea why this is necessary
        val bonus: Int = (8 * context.resources.displayMetrics.density).toInt()
        w = frame.width
        h = (40 * context.resources.displayMetrics.density).toInt()
        x = 0
        y = frame.height - (h + MapApplication.navigationBarHeight!! + MapApplication.statusBarHeight!! + bonus)

        progressLabel.setFrame(x, y, w, h)
    }
}