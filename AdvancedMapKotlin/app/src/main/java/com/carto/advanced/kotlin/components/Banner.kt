package com.carto.advanced.kotlin.components

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 27/07/2017.
 */
class Banner(context: Context) : BaseView(context) {

    val leftImage = ImageView(context)
    val label = TextView(context)
    var rightImage = ImageView(context)

    init {
        setBackgroundColor(Colors.darkTransparentGray)

        leftImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
        leftImage.adjustViewBounds = true
        leftImage.setImageResource(R.drawable.icon_info_white)
        addView(leftImage)

        label.gravity = Gravity.CENTER
        label.setTextColor(Color.WHITE)
        label.textSize = 12.0f
        addView(label)

        rightImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
        rightImage.adjustViewBounds = true
    }

    override fun layoutSubviews() {

        val imagePadding: Int = frame.height / 4
        val imageSize: Int = frame.height - 2 * imagePadding
        var x: Int = imagePadding
        var y: Int = imagePadding
        var w: Int = imageSize
        var h: Int = w

        leftImage.setFrame(x, y, w, h)

        x += w + imagePadding
        w = frame.width - (2 * w + 4 * imagePadding)

        label.setFrame(x, y, w, h)

        x += w + imagePadding
        w = imageSize

        rightImage.setFrame(x, y, w, h)
    }

    fun setText(text: String) {
        label.text = text
        alpha = 1.0f
    }

    fun setRightItem(resource: Int) {
        rightImage.setImageResource(resource)
        addView(rightImage)
    }

    fun setRightItem(image: ImageView) {
        if (rightImage.parent != null) {
            removeView(rightImage)
        }

        rightImage = image
        addView(rightImage)
        layoutSubviews()
    }
}