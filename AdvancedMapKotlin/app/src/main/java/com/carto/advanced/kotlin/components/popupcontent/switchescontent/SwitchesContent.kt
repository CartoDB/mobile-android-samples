package com.carto.advanced.kotlin.components.popupcontent.switchescontent

import com.carto.advanced.kotlin.sections.base.views.BaseView
import android.content.Context

/**
 * Created by aareundo on 17/10/2017.
 */
class SwitchesContent(context: Context) : BaseView(context) {

    val buildingsSwitch = SwitchWithLabel(context)
    val textsSwitch = SwitchWithLabel(context)

    init {

        addView(buildingsSwitch)
        buildingsSwitch.label.text = "3D Buildings"

        addView(textsSwitch)
        textsSwitch.label.text = "3D Texts"
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding = (5 * getDensity()).toInt()

        val x = padding
        var y = padding
        val w = frame.width - 2 * padding
        val h = buildingsSwitch.switch.height

        buildingsSwitch.setFrame(x, y, w, h)

        y += h + padding

        textsSwitch.setFrame(x, y, w, h)
    }
}