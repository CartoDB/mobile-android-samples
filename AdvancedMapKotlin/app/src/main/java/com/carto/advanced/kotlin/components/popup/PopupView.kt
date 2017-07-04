package com.carto.advanced.kotlin.components.popup

import android.content.Context
import com.carto.advanced.kotlin.sections.base.base.BaseView

/**
 * Created by aareundo on 04/07/2017.
 */
class PopupView(context: Context) : BaseView(context) {

    val header = SlideInPopupHeader(context)

    override fun layoutSubviews() {

    }
}