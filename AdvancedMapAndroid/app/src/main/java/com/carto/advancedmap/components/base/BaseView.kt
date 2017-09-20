package com.carto.advanced.kotlin.sections.base.views

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import com.carto.advanced.kotlin.sections.base.utils.CGRect

/**
 * Created by aareundo on 30/06/2017.
 */
open class BaseView(context: android.content.Context) : android.widget.RelativeLayout(context) {

    var frame: com.carto.advanced.kotlin.sections.base.utils.CGRect = com.carto.advanced.kotlin.sections.base.utils.CGRect.Companion.empty

    override fun setBackground(background: android.graphics.drawable.Drawable?) {

        if (isJellybeanOrHigher()) {
            super.setBackground(background)
        } else {
            if (background is android.graphics.drawable.ColorDrawable) {
                val color = background.color
                setBackgroundColor(color)
            }
        }
    }

    override fun setBackgroundColor(color: Int) {

        val drawable = android.graphics.drawable.GradientDrawable()
        drawable.setColor(color)
        background = drawable
    }

    fun getMetrics(): android.util.DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun getDensity(): Float {
        return getMetrics().density
    }

    fun setCornerRadius(radius: Float) {

        if (isJellybeanOrHigher()) {
            (background as android.graphics.drawable.GradientDrawable).cornerRadius = radius
        }
    }

    fun setBorderColor(width: Int, color: Int) {
        (background as android.graphics.drawable.GradientDrawable).setStroke(width, color)
    }

    fun setFrame(x: Int, y: Int, width: Int, height: Int) {
        this.frame = com.carto.advanced.kotlin.sections.base.utils.CGRect(x, y, width, height)

        val params = android.widget.RelativeLayout.LayoutParams(width, height)
        params.leftMargin = x
        params.topMargin = y

        layoutParams = params

        layoutSubviews()
    }

    fun matchParent() {
        val metrics = context.resources.displayMetrics
        setFrame(0, 0, metrics.widthPixels, metrics.heightPixels)
    }

    fun setMainViewFrame() {
        val manager = context.getSystemService(android.content.Context.WINDOW_SERVICE) as android.view.WindowManager
        val size = android.graphics.Point()

        manager.defaultDisplay.getSize(size)

//        frame = CGRect(0, 0, size.x, size.y)
        frame = com.carto.advanced.kotlin.sections.base.utils.CGRect(0, 0, size.x, size.y - (getActionBarHeight() + getStatusBarHeight()))
    }

    fun getNavBarHeight(): Int {
        return getHeightOf("navigation_bar_height")
    }

    fun getStatusBarHeight(): Int {
        return getHeightOf("status_bar_height")
    }

    fun getHeightOf(of: String): Int {
        var result = 0
        val resourceId = resources.getIdentifier(of, "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getActionBarHeight(): Int {
        val tv = android.util.TypedValue()
        context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
        return resources.getDimensionPixelSize(tv.resourceId)
    }

    open fun layoutSubviews() {

    }

    fun isJellybeanOrHigher(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN
    }

    fun sendToBack(view: android.view.View) {

        for (i in 0..childCount) {
            val child = getChildAt(i)

            if (child != null && !child.equals(view)) {
                child.bringToFront()
            }
        }
    }

    fun disableAutoFocusOfTextField() {
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.requestFocus()
    }

    fun closeKeyboard() {
        // Check if no view has focus:
        val view = (context as android.app.Activity).currentFocus

        if (view != null) {
            val service = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
            val manager = service as android.view.inputmethod.InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

