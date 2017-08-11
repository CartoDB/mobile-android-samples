package com.carto.advanced.kotlin.components

import android.content.Context
import android.view.MotionEvent

/**
 * Created by aareundo on 19/07/2017.
 */
class SwitchButton(context: Context, imageResource1: Int, imageResource2: Int) : PopupButton(context, imageResource1) {

    val resource1 = imageResource1
    val resource2 = imageResource2

    val isOnline: Boolean
        get() = imageView.tag == resource1

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            toggle()
        }

        return super.onTouchEvent(event)
    }

    fun toggle() {
        if (imageView.tag == resource1) {
            setImageResource(resource2)
        } else {
            setImageResource(resource1)
        }
    }
}