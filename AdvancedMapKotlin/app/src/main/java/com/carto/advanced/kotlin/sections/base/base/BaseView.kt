package com.carto.advanced.kotlin.sections.base.base

import android.content.Context
import android.os.Build
import android.widget.RelativeLayout
import com.carto.ui.MapView

/**
 * Created by aareundo on 30/06/2017.
 */
open class BaseView(context: Context) : RelativeLayout(context) {
    var frame: CGRect = CGRect.Companion.empty

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

    open fun layoutSubviews() {

    }

    fun isJellybeanOrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }
}
