package com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.widget.TextView
import com.carto.advanced.kotlin.model.Language
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 14/07/2017.
 */
class LanguageCell(context: Context) : BaseView(context) {

    val label = TextView(context)

    var item: Language? = null

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

    fun update(city: Language) {
        this.item = city
        label.text = city.name.toUpperCase()
    }
}