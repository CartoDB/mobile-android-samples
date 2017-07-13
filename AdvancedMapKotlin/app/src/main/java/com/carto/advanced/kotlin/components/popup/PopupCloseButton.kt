package com.carto.advanced.kotlin.components.popup

import android.content.Context
import android.widget.ImageView
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame

/**
 * Created by aareundo on 04/07/2017.
 */
class PopupCloseButton(context: Context) : BaseView(context) {

    val image = ImageView(context)

    init {
        image.setImageResource(R.drawable.icon_close)
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(image)
    }

    override fun layoutSubviews() {
        val padding = frame.width / 3
        image.setFrame(padding, padding, frame.width - 2 * padding, frame.height - 2 * padding)
    }
}