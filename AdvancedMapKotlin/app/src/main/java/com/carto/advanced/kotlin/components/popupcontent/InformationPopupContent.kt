package com.carto.advanced.kotlin.components.popupcontent

import android.content.Context
import android.widget.ScrollView
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.base.BaseView
import com.carto.advanced.kotlin.sections.base.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 06/07/2017.
 */
class InformationPopupContent(context: Context) : BaseView(context) {

    val header = TextView(context)

    val content = TextView(context)
    val container = ScrollView(context)

    init {

        header.textSize = 14.0f
        header.setTextColor(Colors.navy)

        addView(header)

        addView(container)

        content.setTextColor(Colors.navy)
        container.addView(content)
    }

    override fun layoutSubviews() {

        val density = context.resources.displayMetrics.density

        val headerHeight: Int = (40 * density).toInt()
        val padding: Int = (5 * density).toInt()

        val x: Int = 2 * padding
        var y: Int = padding
        var h: Int = headerHeight
        val w: Int = frame.width - 4 * padding

        header.setFrame(x, y, w, h)

        y += h + padding
        h = frame.height - (headerHeight + 3 * padding)

        container.setFrame(x, y, w, h)

        // Need to set frame before, so sizeToFit() knows what the width it.
        content.setFrame(0, 0, container.layoutParams.width, container.layoutParams.height)

        content.measure(0, 0)

        content.setFrame(0, 0, container.layoutParams.width, content.measuredHeight)
    }
}