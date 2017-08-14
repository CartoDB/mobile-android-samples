package com.carto.advanced.kotlin.sections.geocoding

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.geocoding.GeocodingResult

/**
 * Created by aareundo on 20/07/2017.
 */
class GeocodingResultCell(context: Context) : BaseView(context) {

    val label = TextView(context)

    val leftPadding = (5 * context.resources.displayMetrics.density).toInt()

    init {
        label.textSize = 15.0f
        label.gravity = Gravity.CENTER_VERTICAL
        label.setTextColor(Color.WHITE)

        addView(label)
    }

    override fun layoutSubviews() {
        label.setFrame(leftPadding, 0, frame.width - 2 * leftPadding, frame.height)
    }

    fun update(item: GeocodingResult) {
        label.text = item.getPrettyAddress()
    }
}