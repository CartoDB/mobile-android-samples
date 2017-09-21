package com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 17/07/2017.
 */
class StylePopupContentSectionItem(context: Context, text: String, resource: Int) : BaseView(context) {

    var imageView = ImageView(context)
    var label = TextView(context)

    init {

        setBackgroundColor(Color.WHITE)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(resource)
        addView(imageView)

        label.text = text
        label.setTextColor(Colors.appleBlue)
        label.textSize = 11.0f

        addView(label)
    }

    override fun layoutSubviews() {

        val padding = (5 * context.resources.displayMetrics.density).toInt()

        val x = borderWidth
        var y = borderWidth
        val w = frame.width - 2 * borderWidth
        var h = (frame.height - 2 * borderWidth) / 3 * 2

        imageView.setFrame(x, y, w, h)

        label.measure(0, 0)

        y += h + padding
        h = label.measuredHeight

        label.setFrame(x, y, w, h)
    }

    val borderWidth = (2 * getDensity()).toInt()

    fun highlight() {
        val width = borderWidth
        setBorderColor(width, Colors.appleBlue)
    }

    fun normalize() {
        setBorderColor(0, Colors.appleBlue)
    }
}