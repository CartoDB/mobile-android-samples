package com.carto.advanced.kotlin.sections.base

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.ScrollView

/**
 * Created by aareundo on 30/06/2017.
 */
open class BaseScrollView(context: Context) : ScrollView(context)  {

    var container: BaseView? = null

    init {
        container = BaseView(context)
        container?.matchParent()
        super.addView(container)
    }
    var frame: CGRect = CGRect.empty

    fun setFrame(x: Int, y: Int, width: Int, height: Int) {
        this.frame = CGRect(x, y, width, height)

        val params = RelativeLayout.LayoutParams(width, height)
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

    override fun addView(child: View?) {
        container?.addView(child)
    }

    override fun removeView(view: View?) {
        container?.removeView(view)
    }
}