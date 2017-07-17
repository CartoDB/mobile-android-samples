package com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent

import android.content.Context
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 17/07/2017.
 */
class StylePopupContentSection(context: Context) : BaseView(context) {

    var header = TextView(context)
    var separator = BaseView(context)

    var list = mutableListOf<StylePopupContentSectionItem>()

    var source: String? = null

    init {

        header.setTextColor(Colors.navy)
        header.textSize = 15.0f

        addView(header)

        separator.setBackgroundColor(Colors.nearWhite)

        addView(separator)
    }

    fun addItem(text: String, imageResource: Int) {
        val item = StylePopupContentSectionItem(context, text, imageResource)
        list.add(item)
        addView(item)
    }

    override fun layoutSubviews() {

        val density = context.resources.displayMetrics.density

        val headerHeight = (20 * density).toInt()
        val padding = (5 * density).toInt()

        var x = padding
        var y = -padding
        var w = frame.width - 2 * padding
        var h = (1 * density).toInt()

        separator.setFrame(x, y, w, h)

        y = 0
        w = frame.width - 3 * padding
        h = headerHeight

        header.setFrame(x, y, w, h)

        y = headerHeight
        h = rowHeight - 2 * padding
        w = (frame.width - 4 * padding)  / 3

        for (item in list) {

            item.setFrame(x, y, w, h)

            x += w + padding

            if (x == frame.width) {
                x = padding
                y += rowHeight
            }
        }

    }

    val rowHeight = (90 * context.resources.displayMetrics.density).toInt()

    fun getCalculatedHeight(): Int {

        if (list.size > 6) {
            return 3 * rowHeight
        }

        if (list.size > 3) {
            return 2 * rowHeight
        }

        return rowHeight
    }
}