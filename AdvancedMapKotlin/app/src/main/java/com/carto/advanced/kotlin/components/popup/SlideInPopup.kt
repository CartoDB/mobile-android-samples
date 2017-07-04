package com.carto.advanced.kotlin.components.popup

import android.content.Context
import com.carto.advanced.kotlin.sections.base.base.BaseView

/**
 * Created by aareundo on 04/07/2017.
 */
class SlideInPopup(context: Context) : BaseView(context) {

    val transparentArea = BaseView(context)
    val popup = PopupView(context)

    init {

    }

    override fun layoutSubviews() {

    }
}