package com.carto.advanced.kotlin.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.base.BaseView
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 03/07/2017.
 */
class ProgressLabel(context: Context) : BaseView(context) {

    var label: TextView = TextView(context)
    var progressBar: BaseView = BaseView(context)

    init {

        setBackgroundColor(Colors.transparentGray)

        addView(label)
        addView(progressBar)
    }

    override fun layoutSubviews() {

    }
}