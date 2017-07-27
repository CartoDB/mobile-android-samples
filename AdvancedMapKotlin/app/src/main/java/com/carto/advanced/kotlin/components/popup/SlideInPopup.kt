package com.carto.advanced.kotlin.components.popup

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import com.carto.advanced.kotlin.MapApplication
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.isLandScape
import com.carto.advanced.kotlin.sections.base.isLargeTablet
import com.carto.advanced.kotlin.sections.base.setFrame

/**
 * Created by aareundo on 04/07/2017.
 */
class SlideInPopup(context: Context) : BaseView(context) {

    val transparentArea = BaseView(context)
    val popup = PopupView(context)

    var hiddenY: Int = -1
    var visibleY: Int = -1

    var content: BaseView? = null

    fun isVisible(): Boolean {
        return transparentArea.alpha > 0.0
    }

    init {
        transparentArea.setBackgroundColor(Color.BLACK)
        transparentArea.alpha = 0.0f
        addView(transparentArea)

        addView(popup)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 11.0f
        }
    }

    override fun layoutSubviews() {

        val x: Int = 0
        var y: Int = 0
        var w: Int = frame.width
        var h: Int = frame.height

        transparentArea.setFrame(x, y, w, h)

        hiddenY = h
        visibleY = h - (h / 5 * 3)

        if (isLandScape() || isLargeTablet()) {
            w = (400 * context.resources.displayMetrics.density).toInt()
            visibleY = 0
        }

        y = visibleY
        popup.setFrame(x, y, w, h)

        hide(0)
    }

    fun setPopupContent(content: BaseView) {

        if (this.content != null) {
            popup.removeView(this.content)
            this.content = null
        }

        this.content = content
        popup.addView(content)

        val x = 0
        val y = popup.header.totalHeight
        val w = popup.frame.width
        val h = popup.frame.height - popup.header.totalHeight

        content.setFrame(x, y, w, h)
    }

    fun show() {
        bringToFront()
        visibility = View.VISIBLE

        animateAlpha(0.5f)
        animateY(visibleY)

        transparentArea.setOnClickListener {
            hide()
        }

        popup.header.closeButton.setOnClickListener {
            hide()
        }
    }

    fun hide(duration: Long = 200) {
        animateAlpha(0.0f, duration)
        animateY(hiddenY, duration)

        transparentArea.setOnClickListener(null)

        popup.header.closeButton.setOnClickListener(null)
    }

    fun animateAlpha(to: Float, duration: Long = 200) {
        val animator = ObjectAnimator.ofFloat(transparentArea, "alpha", to)
        animator.duration = duration
        animator.start()
    }

    fun animateY(to: Int, duration: Long = 200) {

//        popup.setFrame(popup.frame.x, to, popup.frame.width, popup.frame.height)
//        if (to == hiddenY) {
//            visibility = View.GONE
//        }

        val animator = ObjectAnimator.ofFloat(popup, "y", to.toFloat())
        animator.duration = duration
        animator.start()

        animator.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) { }

            override fun onAnimationCancel(animation: Animator?) { }

            override fun onAnimationRepeat(animation: Animator?) { }

            override fun onAnimationEnd(animation: Animator?) {
                if (to == hiddenY) {
                    visibility = View.GONE
                }

            }
        })
    }

}








