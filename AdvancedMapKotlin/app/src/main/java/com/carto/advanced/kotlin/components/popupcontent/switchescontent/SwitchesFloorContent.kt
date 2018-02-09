package com.carto.advanced.kotlin.components.popupcontent.switchescontent

import android.content.Context
import android.view.Gravity
import android.widget.SeekBar
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.sections.base.views.BaseView

/**
 * Created by aareundo on 25/10/2017.
 */
class SwitchesFloorContent(context: Context, val count: Int) : BaseView(context) {

    val list = mutableListOf<SwitchWithLabel>()

    val slider = SeekBar(context)
    val label = TextView(context)

    init {

        addView(slider)

        label.gravity = Gravity.CENTER_HORIZONTAL
        addView(label)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding = (5 * getDensity()).toInt()

        var x = padding
        var y = padding
        var w = frame.width - 2 * padding
        var h = (30 * getDensity()).toInt()
        slider.setFrame(x, y, w, h)

        y += h + padding

        label.setFrame(x, y, w, h)
    }

    fun setProgress(progress: Int) {
        label.text = progress.toString()
        slider.progress = progress
    }
}