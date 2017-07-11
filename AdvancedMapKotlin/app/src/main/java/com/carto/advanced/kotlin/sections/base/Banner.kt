package com.carto.advanced.kotlin.sections.base

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 11/07/2017.
 */
class Banner(context: Context) : BaseView(context) {

    val label: TextView = TextView(context)

    init {
        val drawable = GradientDrawable()
        drawable.setColor(Colors.transparentGray)
        background = drawable

        visibility = View.GONE

        label.setTextColor(Color.WHITE)
        label.gravity = Gravity.CENTER
        label.typeface = Typeface.DEFAULT_BOLD

        addView(label)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        label.setFrame(0, 0, frame.width, frame.height)


        val padding = frame.height / 5

        val h = frame.height - 2 * padding
        val w = h

        itemRight?.setFrame(frame.width - (w + padding), padding, w, h)
    }

    fun setText(text: String) {
        label.text = text
        visibility = View.VISIBLE
    }

    var itemRight: ImageView? = null

    fun setRightItem(view: ImageView) {
        itemRight = view
        addView(view)
    }
}