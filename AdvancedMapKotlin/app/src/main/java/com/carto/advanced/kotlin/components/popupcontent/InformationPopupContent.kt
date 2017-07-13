package com.carto.advanced.kotlin.components.popupcontent

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.JustifiedTextView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 06/07/2017.
 */
class InformationPopupContent(context: Context) : BaseView(context) {

    val header = TextView(context)

    val content = JustifiedTextView(context)
    val container = ScrollView(context)

    init {

        header.textSize = 18.0f
        header.setTextColor(Colors.navy)
        header.typeface = Typeface.DEFAULT_BOLD

        addView(header)

        addView(container)

        content.setTextColor(Colors.navy)
        content.textSize = 15.0f
        container.addView(content)

        var layout = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

//        content.layoutParams = layout

    }

    override fun layoutSubviews() {

        val density = context.resources.displayMetrics.density

        val headerHeight: Int = (40 * density).toInt()
        val padding: Int = (5 * density).toInt()

        val x: Int = 2 * padding
        var y: Int = padding
        val h: Int = headerHeight
        val w: Int = frame.width - 4 * padding

        header.setFrame(x, y, w, h)

        var layout = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

//        content.layoutParams = layout

        layout = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layout.topMargin = y
        layout.leftMargin = 2 * padding
        layout.rightMargin = 2 * padding

        container.layoutParams = layout
    }

    fun setTitle(text: String) {
        header.text = text
        layoutSubviews()
    }

    fun setDescription(text: String) {
        content.text = text
        layoutSubviews()
    }

}