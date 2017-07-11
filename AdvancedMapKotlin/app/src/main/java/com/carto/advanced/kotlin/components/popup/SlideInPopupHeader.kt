package com.carto.advanced.kotlin.components.popup

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 04/07/2017.
 */
class SlideInPopupHeader(context: Context) : BaseView(context) {

    val totalHeight: Int = (context.resources.displayMetrics.density * 40).toInt()

    val backButton = PopupBackButton(context)
    val label = TextView(context)
    val closeButton = PopupCloseButton(context)

    fun setText(text: String) {
        label.text = text
    }

    init {
        label.gravity = Gravity.CENTER
        label.textSize = 11f
        label.setTextColor(Colors.navy)
        addView(label)

        backButton.text.textSize = label.textSize
        backButton.text.setTextColor(Colors.navy)
        backButton.setBackgroundColor(Color.WHITE)
        addView(backButton)
        backButton.visibility = View.GONE

        addView(closeButton)
    }

    override fun layoutSubviews() {

        val padding: Int = (10 * context.resources.displayMetrics.density).toInt()

        var x: Int = padding
        val y: Int = 0
        var w: Int = label.measuredWidth
        val h: Int = frame.height

        label.setFrame(x, y, w, h)
        backButton.setFrame(x, y, w, h)

        w = h
        x = frame.width - w

        closeButton.setFrame(x, y, w, h)
    }
}