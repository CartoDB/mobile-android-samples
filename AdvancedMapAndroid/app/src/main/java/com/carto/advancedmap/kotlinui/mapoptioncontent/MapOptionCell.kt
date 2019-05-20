package com.carto.advanced.kotlin.components.popupcontent.mapoptioncontent

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.widget.CheckBox
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advancedmap.model.MapOption

class MapOptionCell(context: Context) : BaseView(context) {

    val label = TextView(context)

    var item: MapOption? = null

    init {
        label.textSize = 15.0f
        label.gravity = Gravity.CENTER_VERTICAL
        label.typeface = Typeface.DEFAULT_BOLD

        addView(label)
    }

    val leftPadding = (15 * context.resources.displayMetrics.density).toInt()

    override fun layoutSubviews() {
        label.setFrame(leftPadding, 0, frame.width - leftPadding, frame.height)
    }

    fun update(option: MapOption) {
        this.item = option
        if (option.value) {
            label.text = option.name + " \u2713"
        } else {
            label.text = option.name
        }
    }
}