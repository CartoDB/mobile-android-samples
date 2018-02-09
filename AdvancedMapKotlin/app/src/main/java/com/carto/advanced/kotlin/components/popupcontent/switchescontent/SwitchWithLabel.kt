package com.carto.advanced.kotlin.components.popupcontent.switchescontent

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.view.Gravity
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 17/10/2017.
 */
class SwitchWithLabel(context: Context) : BaseView(context) {

    val label = TextView(context)
    val switch = SwitchCompat(context)

    init {
        label.gravity = Gravity.CENTER_VERTICAL
        label.setTextColor(Colors.darkGrayInt)
        addView(label)

        addView(switch)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding = (5 * getDensity()).toInt()
        val assumedWidth = switch.height * 2

        var x = padding
        val y = 0
        val h = switch.height
        var w = frame.width - (assumedWidth + 2 * padding)

        label.setFrame(x, y, w, h)

        x += w
        w = assumedWidth

        switch.setFrame(x, y, w, h)
    }

    fun uncheck() {
        switch.isChecked = false
    }

    fun check() {
        switch.isChecked = true
    }

    fun enable() {
        switch.isEnabled = true
    }

    fun disable() {
        switch.isEnabled = false
    }
}