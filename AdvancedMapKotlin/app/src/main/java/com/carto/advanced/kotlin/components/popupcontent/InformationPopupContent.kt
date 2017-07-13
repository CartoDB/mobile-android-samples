package com.carto.advanced.kotlin.components.popupcontent

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.method.ScrollingMovementMethod
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

    val content = TextView(context)

    init {

        header.textSize = 18.0f
        header.setTextColor(Colors.navy)
        header.typeface = Typeface.DEFAULT_BOLD

        addView(header)

        content.setTextColor(Colors.navy)
        content.textSize = 15.0f
        content.maxLines = Int.MAX_VALUE
        content.movementMethod = ScrollingMovementMethod()
        addView(content)

    }

    override fun layoutSubviews() {

        val density = context.resources.displayMetrics.density

        val headerHeight: Int = (40 * density).toInt()
        val padding: Int = (5 * density).toInt()
        var leftPadding = 2 * padding

        val x: Int = 2 * padding
        var y: Int = padding
        var h: Int = headerHeight
        val w: Int = frame.width - 4 * padding

        header.setFrame(x, y, w, h)

        y += h
        h = frame.height - (headerHeight + padding)

        content.setFrame(x, y, w, h)
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