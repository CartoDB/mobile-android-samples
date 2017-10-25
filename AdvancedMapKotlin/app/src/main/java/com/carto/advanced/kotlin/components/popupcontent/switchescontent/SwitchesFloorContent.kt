package com.carto.advanced.kotlin.components.popupcontent.switchescontent

import android.content.Context
import com.carto.advanced.kotlin.sections.base.views.BaseView

/**
 * Created by aareundo on 25/10/2017.
 */
class SwitchesFloorContent(context: Context, val count: Int) : BaseView(context) {

    val list = mutableListOf<SwitchWithLabel>()

    init {

        for (i in 0..count) {
            val switch = SwitchWithLabel(context)
            switch.label.text = "Floor " + i.toString()
            switch.id = i
            list.add(switch)
            addView(switch)
        }
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding = (5 * getDensity()).toInt()

        val x = padding
        var y = padding
        val w = frame.width - 2 * padding
        val h = list[0].switch.height

        for (i in 0..count) {
            val switch = list[i]
            switch.setFrame(x, y, w, h)
            y += h + padding
        }
    }
}