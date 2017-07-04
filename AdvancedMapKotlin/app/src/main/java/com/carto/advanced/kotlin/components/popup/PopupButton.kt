package com.carto.advanced.kotlin.components.popup

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import com.carto.advanced.kotlin.sections.base.base.BaseView
import com.carto.advanced.kotlin.sections.base.base.setFrame

/**
 * Created by aareundo on 04/07/2017.
 */
class PopupButton(context: Context, imageResource: Int) : BaseView(context) {

    val imageView = ImageView(context)

    init {

        setBackgroundColor(Color.WHITE)

        imageView.setImageResource(imageResource)
        imageView?.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(imageView)
    }

    override fun layoutSubviews() {

        val padding: Int = (5 * context.resources.displayMetrics.density).toInt()

        imageView.setFrame(padding, padding, frame.width - 2 * padding, frame.height - 2 * padding)
    }

}