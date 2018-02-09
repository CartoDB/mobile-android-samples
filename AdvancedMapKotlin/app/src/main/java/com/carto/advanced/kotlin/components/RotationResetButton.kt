package com.carto.advanced.kotlin.components

import android.app.Activity
import android.content.Context
import com.carto.advanced.kotlin.R

/**
 * Created by aareundo on 27/07/2017.
 */
class RotationResetButton(context: Context) : PopupButton(context, R.drawable.icon_compass) {

    fun rotate(angle: Float) {
        (context as Activity).runOnUiThread {
            imageView.rotation = angle
        }
    }
}