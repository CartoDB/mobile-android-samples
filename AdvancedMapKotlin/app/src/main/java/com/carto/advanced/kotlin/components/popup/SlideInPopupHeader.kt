package com.carto.advanced.kotlin.components.popup

import android.content.Context
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.base.BaseView

/**
 * Created by aareundo on 04/07/2017.
 */
class SlideInPopupHeader(context: Context) : BaseView(context) {

    val totalHeight: Int = (context.resources.displayMetrics.density * 40).toInt()

    val backButton = PopupBackButton(context)
    val label = TextView(context)
    val closeButton = PopupCloseButton(context)
}