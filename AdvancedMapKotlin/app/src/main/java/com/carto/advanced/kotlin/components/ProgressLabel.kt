package com.carto.advanced.kotlin.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.animation.AlphaAnimation
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.base.BaseView
import com.carto.advanced.kotlin.sections.base.base.setFrame
import com.carto.advanced.kotlin.utils.Colors
import com.carto.core.MapPos
import java.lang.Math.round

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

        label.setTextColor(Color.WHITE)
        addView(label)

        progressBar.setBackgroundColor(Colors.appleBlue)
        addView(progressBar)
    }

    override fun layoutSubviews() {
        label.setFrame(0, 0, frame.width, frame.height)
    }

    fun update(progress: Float, position: MapPos) {

        label.text = "DOWNLOADING" + position.toString() + " (" + (round(progress * 10) / 10).toString() + "%)"

        updateProgressBar(progress)
    }

    fun update(text: String, progress: Float) {
        label.text = text
        updateProgressBar(progress)
    }

    fun update(text: String) {
        label.text = text
    }

    fun complete(message: String) {

        if (!isVisible()) {
            show()
        }

        label.text = message
    }

    fun complete(position: MapPos) {
        label.text = "DOWNLOAD OF" + position.toString() + "COMPLETED"
    }

    fun updateProgressBar(progress: Float) {

        val width: Int = ((frame.width * progress) / 100).toInt()
        val height: Int = (3 * context.resources.displayMetrics.density).toInt()
        val y: Int = frame.height - height

        progressBar.setFrame(0, y, width, height)
    }

    fun positionToString(position: MapPos): String {

        val lat = round(position.x * 100) / 100
        val lon = round(position.y * 100) / 100
        return  " [lat: " + lat.toString() + ", lon: " + lon.toString() + "] "
    }

    fun hide() {
        if (isVisible()) {
            animateAlpha(0f)
        }
    }

    fun show() {
        if (!isVisible()) {
            animateAlpha(1.0f)
        }
    }

    fun animateAlpha(alpha: Float) {
        val animation = AlphaAnimation(this.alpha, alpha)
        animation.duration = 100

        animation.start()
    }
}