package com.carto.advanced.kotlin.components

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.MotionEvent
import android.widget.ImageView
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame

/**
 * Created by aareundo on 04/07/2017.
 */
open class PopupButton(context: Context, imageResource: Int) : BaseView(context) {

    val imageView = ImageView(context)

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 10.0f
        }

        setBackgroundColor(Color.WHITE)

        setImageResource(imageResource)

        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView.adjustViewBounds = true

        addView(imageView)
    }

    override fun layoutSubviews() {

        setCornerRadius((frame.width / 2).toFloat())

        val padding: Int = (15 * context.resources.displayMetrics.density).toInt()
        val imageSize = frame.width - 2 * padding

        imageView.setFrame(padding, padding, imageSize, imageSize)
    }

    fun setImageResource(resource: Int) {
        imageView.setImageResource(resource)
        imageView.tag = resource
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (!isEnabled) {
            return true
        }

        if (event?.action == MotionEvent.ACTION_DOWN) {
            alpha = 0.5f
        } else if (event?.action == MotionEvent.ACTION_UP) {
            alpha = 1.0f
            callOnClick()
        } else if (event?.action == MotionEvent.ACTION_CANCEL) {
            alpha = 1.0f
        }

        return super.onTouchEvent(event)
    }

    fun disable() {
        isEnabled = false
        alpha = 0.5f
    }

    fun enable() {
        isEnabled = true
        alpha = 1.0f
    }
}