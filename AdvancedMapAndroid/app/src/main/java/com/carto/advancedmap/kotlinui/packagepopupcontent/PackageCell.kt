package com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.CGRect
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.utils.Colors
import com.carto.advancedmap.R
import com.carto.packagemanager.PackageAction
import com.carto.packagemanager.PackageStatus
import com.carto.advancedmap.model.Package

/**
 * Created by aareundo on 12/07/2017.
 */
class PackageCell(context: Context) : BaseView(context) {

    var item: Package? = null

    val textLabel = TextView(context)

    val title = TextView(context)
    val subtitle = TextView(context)
    val statusIndicator = TextView(context)
    val forwardIcon = ImageView(context)

    val progressIndicator = BaseView(context)

    val titleSize = 13.0f
    val titleColor = Colors.navy

    init {

        textLabel.textSize = titleSize
        textLabel.setTextColor(titleColor)
        textLabel.gravity = Gravity.CENTER_VERTICAL
        textLabel.typeface = Typeface.DEFAULT_BOLD
        addView(textLabel)

        title.textSize = titleSize
        title.setTextColor(titleColor)
        title.typeface = Typeface.DEFAULT_BOLD
        addView(title)

        subtitle.textSize = titleSize - 2
        subtitle.setTextColor(Color.LTGRAY)
        addView(subtitle)

        statusIndicator.gravity = Gravity.CENTER
        statusIndicator.setTextColor(Colors.appleBlue)
        statusIndicator.textSize = titleSize - 1
        statusIndicator.typeface = Typeface.DEFAULT_BOLD

        val drawable = GradientDrawable()
        drawable.cornerRadius = 3 * getDensity()
        drawable.setStroke(1, Colors.appleBlue)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            statusIndicator.background = drawable
        } else {
            @Suppress("DEPRECATION")
            statusIndicator.setBackgroundDrawable(drawable)
        }

        addView(statusIndicator)

        forwardIcon.setImageResource(R.drawable.icon_forward_blue)
        forwardIcon.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(forwardIcon)

        progressIndicator.setBackgroundColor(Colors.appleBlue)
        addView(progressIndicator)
    }

    val leftPadding: Int = (15 * context.resources.displayMetrics.density).toInt()

    override fun layoutSubviews() {

        if (item == null) {
            return
        }

        if (item!!.isGroup()) {

            title.setFrame(0, 0, 0, 0)
            subtitle.setFrame(0, 0, 0, 0)
            statusIndicator.setFrame(0, 0, 0, 0)
            progressIndicator.setFrame(0, 0, 0, 0)

            val h: Int = frame.height / 3
            val w: Int = h / 2
            val x: Int = frame.width - (w + leftPadding)
            val y: Int = frame.height / 2 - h / 2

            forwardIcon.setFrame(x, y, w, h)
            textLabel.setFrame(leftPadding, 0, frame.width, frame.height)
            return
        }

        title.measure(0, 0)
        subtitle.measure(0, 0)
        statusIndicator.measure(0, 0)

        val topPadding: Int = (frame.height - (title.measuredHeight + subtitle.measuredHeight)) / 2
        val titleWidth: Int = (frame.width * 0.66).toInt()

        var x: Int = leftPadding
        var y: Int = topPadding
        var w: Int = titleWidth
        var h: Int = title.measuredHeight

        title.setFrame(x, y, w, h)

        y += h

        subtitle.setFrame(x, y, w, h)

        w = (80 * context.resources.displayMetrics.density).toInt()
        h = (frame.height / 5 * 3.1).toInt()
        x = frame.width - (w + leftPadding)
        y = frame.height / 2 - h / 2

        statusIndicator.setFrame(x, y, w, h)

        progressIndicator.setFrame(
                progressIndicator.frame.x, progressIndicator.frame.y,
                progressIndicator.frame.width, progressIndicator.frame.height
        )
    }

    fun update(item: Package) {

        this.item = item

        if (item.isGroup()) {
            // It's a package group. These are displayed with a single label
            textLabel.text = item.name?.toUpperCase()
            forwardIcon.visibility = View.VISIBLE

            if (item.isCustomRegionFolder()) {
                textLabel.setTextColor(Colors.appleBlue)
            } else {
                textLabel.setTextColor(titleColor)
            }

            return
        }

        forwardIcon.visibility = View.GONE

        // "Hide" the original label, as these aren't used in advanced cells
        textLabel.text = ""

        title.text = item.name?.toUpperCase()
        subtitle.text = item.getStatusText()

        val action = item.getActionText()
        statusIndicator.text = action

        var width = 0
        if (action == Package.ACTION_DOWNLOAD) {
            width = (1.2 * context.resources.displayMetrics.density).toInt()
        }

        (statusIndicator.background as GradientDrawable).setStroke(width, Colors.appleBlue)

        if (this.item?.status == null) {
            progressIndicator.frame = CGRect.empty
            return
        } else {
            updateProgress(this.item?.status?.progress!!)
        }

        if (this.item?.status?.currentAction != PackageAction.PACKAGE_ACTION_DOWNLOADING) {
            progressIndicator.frame = CGRect.empty
        }
    }

    fun update(status: PackageStatus) {
        this.item!!.status = status
        update(this.item!!)
        updateProgress(status.progress)
    }

    fun update(item: Package, progress: Float) {
        update(item)
        updateProgress(progress)

    }

    fun updateProgress(progress: Float) {

        val width = ((frame.width - 2 * leftPadding) * progress / 100).toInt()
        val height = (1.5 * context.resources.displayMetrics.density).toInt()
        val y = frame.height - (5 * context.resources.displayMetrics.density).toInt()
        val x = leftPadding

        progressIndicator.setFrame(x, y, width, height)
    }
}