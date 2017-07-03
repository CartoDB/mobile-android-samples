package com.carto.advanced.kotlin.sections.base.base

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import com.carto.ui.MapView

/**
 * Created by aareundo on 03/07/2017.
 */

fun MapView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun TextView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun ImageView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun Switch.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}