package com.carto.advanced.kotlin.sections.base

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
import android.widget.RelativeLayout

/**
 * Created by aareundo on 30/06/2017.
 */
open class BaseView(context: Context) : RelativeLayout(context) {

    var frame: CGRect = CGRect.Companion.empty

    override fun setBackground(background: Drawable?) {

        if (isJellybeanOrHigher()) {
            super.setBackground(background)
        } else {
            if (background is ColorDrawable) {
                val color = background.color
                setBackgroundColor(color)
            }
        }
    }

    override fun setBackgroundColor(color: Int) {

        val drawable = GradientDrawable()
        drawable.setColor(color)
        background = drawable
    }

    fun getMetrics(): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun setCornerRadius(radius: Float) {

        if (isJellybeanOrHigher()) {
            (background as GradientDrawable).cornerRadius = radius
        }
    }

    fun setFrame(x: Int, y: Int, width: Int, height: Int) {
        this.frame = CGRect(x, y, width, height)

        val params = LayoutParams(width, height)
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
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()

        manager.defaultDisplay.getSize(size)

        frame = CGRect(0, 0, size.x, size.y)
//        frame = CGRect(0, 0, size.x, size.y - (getActionBarHeight() + getStatusBarHeight()))
    }

    fun getNavBarHeight(): Int {
        return getHeightOf("navigation_bar_height")
    }

    fun getStatusBarHeight(): Int {
        return  getHeightOf("status_bar_height")
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
        val tv = TypedValue()
        context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
        return resources.getDimensionPixelSize(tv.resourceId)
    }

    open fun layoutSubviews() {

    }

    fun isJellybeanOrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    fun sendToBack(view: View) {

        for (i in 0..childCount) {
            val child = getChildAt(i)

            if (child != null && !child.equals(view)) {
                child.bringToFront()
            }
        }
    }
}
