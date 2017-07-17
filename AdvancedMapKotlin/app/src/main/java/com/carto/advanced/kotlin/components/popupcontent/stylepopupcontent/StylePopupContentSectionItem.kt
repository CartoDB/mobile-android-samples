package com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 17/07/2017.
 */
class StylePopupContentSectionItem(context: Context, text: String, resource: Int) : BaseView(context) {

    var imageView = ImageView(context)
    var label = TextView(context)

    init {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(resource)

        addView(imageView)

        label.text = text
        label.setTextColor(Colors.appleBlue)
        label.textSize = 13.0f

        addView(label)
    }

    override fun layoutSubviews() {

        val padding = (5 * context.resources.displayMetrics.density).toInt()

        val x = 0
        var y = 0
        val w = frame.width
        var h = frame.height / 3 * 2

        imageView.setFrame(x, y, w, h)

        label.measure(0, 0)

        y += h + padding
        h = label.measuredHeight

        label.setFrame(x, y, w, h)
    }
}