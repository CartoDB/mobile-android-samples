package com.carto.advanced.kotlin.components.popupcontent.citypopupcontent

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.widget.TextView
import com.carto.advanced.kotlin.model.City
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 13/07/2017.
 */
class CityCell(context: Context) : BaseView(context) {

    val label = TextView(context)

    var item: City? = null

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

    fun update(city: City) {
        this.item = city
        val text = city.name.toUpperCase()

        if (city.existsLocally) {
            label.setTextColor(Colors.appleBlue)
            label.text = "$text (${city.size} MB)"
        } else {
            label.setTextColor(Colors.navy)
            label.text = text
        }
    }
}