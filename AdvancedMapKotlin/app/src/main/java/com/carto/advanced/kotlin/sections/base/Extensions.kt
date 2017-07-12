package com.carto.advanced.kotlin.sections.base

import android.graphics.Color
import android.view.View
import android.widget.*
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

fun ScrollView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun ListView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun BaseView.isLargeTablet(): Boolean {

    var greater = height
    var lesser = width

    if (isLandScape()) {
        greater = width
        lesser = height
    }

    if (context.resources.displayMetrics.density > 2.5) {
        // If density is too large, it'll be a phone
        return false
    }

    return greater > 1920 && lesser > 1080
}

fun BaseView.isLandScape(): Boolean {
    return frame.width > frame.height
}

fun Int.toCartoColor(): com.carto.graphics.Color {

    val r = Color.red(this).toShort()
    val g = Color.green(this).toShort()
    val b = Color.blue(this).toShort()
    val a = Color.alpha(this).toShort()

    return com.carto.graphics.Color(r, g, b, a)
}