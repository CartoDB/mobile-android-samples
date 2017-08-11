package com.carto.advanced.kotlin.components

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.utils.Colors
import com.carto.core.MapPos
import java.lang.Math.round
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by aareundo on 03/07/2017.
 */
class ProgressLabel(context: Context) : BaseView(context) {

    var label: TextView = TextView(context)
    var progressBar: BaseView = BaseView(context)

    var height: Int? = -1

    fun isVisible(): Boolean {
        return alpha >= 1
    }

    init {

        setBackgroundColor(Colors.transparentGray)

        label.gravity = Gravity.CENTER
        label.setTextColor(Color.WHITE)
        addView(label)

        progressBar.setBackgroundColor(Colors.appleBlue)
        addView(progressBar)
    }

    override fun layoutSubviews() {
        label.setFrame(0, 0, frame.width, frame.height)
    }

    @SuppressLint("SetTextI18n")
    fun update(progress: Float, position: MapPos) {

        label.text = "DOWNLOADING" + position.toString() + " (" + (round(progress * 10) / 10).toString() + "%)"

        updateProgressBar(progress)
    }

    fun update(text: String, progress: Float) {
        update(text)
        updateProgressBar(progress)
    }

    fun update(text: String) {
        if (!isVisible()) {
            show()
        }
        label.text = text.toUpperCase()
    }

    fun complete(message: String) {

        show()

        label.text = message

        val timer = Timer()
        timer.schedule(5000) {
            if (context is BaseActivity) {
                (context as BaseActivity).runOnUiThread {
                    hide()
                }
            }
        }
    }

    fun updateProgressBar(progress: Float) {

        val width: Int = ((frame.width * progress) / 100).toInt()
        val height: Int = (3 * context.resources.displayMetrics.density).toInt()
        val y: Int = frame.height - height

        progressBar.setFrame(0, y, width, height)
    }

    fun hide() {
        animateAlpha(0f)
    }

    fun show() {
        animateAlpha(1.0f)
    }

    fun animateAlpha(to: Float) {
        val animator = ObjectAnimator.ofFloat(this, "alpha", to)
        animator.duration = 300
        animator.start()
    }
}