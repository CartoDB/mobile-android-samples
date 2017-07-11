package com.carto.advanced.kotlin.components

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.Switch
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 03/07/2017.
 */
class StateSwitch(context: Context) : BaseView(context) {

    val label: TextView = TextView(context)
    val switch: Switch = Switch(context)

    var switchHeight: Int = 60
    var switchWidth: Int = 200

    init {

        setBackgroundColor(Colors.transparentGray)

        label.setTextColor(Color.WHITE)
        label.gravity = Gravity.CENTER
        label.textSize = 15f

        addView(label)
        addView(switch)

        setText("ONLINE")
        switch.isChecked = true

        switch.measure(0, 0)

        switchHeight = switch.measuredHeight
        switchWidth =  switch.measuredWidth
    }

    fun setText(text: String) {
        label.text = text
        label.measure(0, 0)
        layoutSubviews()
    }

    val padding: Int = 5
    val textPadding: Int = 15

    fun getTotalWidth(): Int {
        return label.measuredWidth + switchWidth +  padding + 2 * textPadding
    }

    fun getTotalHeight(): Int {
        return  switchHeight + 2 * padding
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        var x: Int = textPadding
        val y: Int = padding
        var w: Int = label.measuredWidth
        val h: Int = frame.height - 2 * padding

        label.setFrame(x, y, w, h)

        x += w + textPadding
        w = switchWidth

        switch.setFrame(x, y, w, h)
    }

}