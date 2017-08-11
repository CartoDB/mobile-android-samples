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
    val type = TextView(context)

    val leftPadding = (5 * context.resources.displayMetrics.density).toInt()

    init {
        label.textSize = 15.0f
        label.gravity = Gravity.CENTER_VERTICAL
        label.setTextColor(Color.WHITE)
        addView(label)

        type.textSize = 12.0f
        type.gravity = Gravity.CENTER_VERTICAL
        type.setTextColor(Color.LTGRAY)
        addView(type)
    }

    override fun layoutSubviews() {
        val width = frame.width - 2 * leftPadding
        val split = frame.height * 3 / 5
        label.setFrame(leftPadding, 0, width, split)
        type.setFrame(leftPadding, split, width, frame.height)
    }

    fun update(item: GeocodingResult) {
        label.text = item.getPrettyAddress().toUpperCase()
        if (item.address.name != "") {
            type.text = "Point of Interest"
        } else if (item.address.houseNumber != "") {
            type.text = "Address"
        } else if (item.address.street != "") {
            type.text = "Street"
        } else if (item.address.neighbourhood != "") {
            type.text = "Neighbourhood"
        } else if (item.address.locality != "") {
            type.text = "Town/village"
        } else if (item.address.county != "") {
            type.text = "County"
        } else if (item.address.region != "") {
            type.text = "Region"
        } else if (item.address.country != "") {
            type.text = "Country"
        } else {
            type.text = ""
        }
    }
}