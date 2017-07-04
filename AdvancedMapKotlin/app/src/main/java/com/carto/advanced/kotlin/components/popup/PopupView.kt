package com.carto.advanced.kotlin.components.popup

import android.content.Context
import android.graphics.Color
import com.carto.advanced.kotlin.sections.base.base.BaseView

/**
 * Created by aareundo on 04/07/2017.
 */
class PopupView(context: Context) : BaseView(context) {

    val header = SlideInPopupHeader(context)

    init {
        setBackgroundColor(Color.WHITE)

        addView(header)
    }

    override fun layoutSubviews() {
        header.setFrame(0, 0, frame.width, header.totalHeight)
    }
}