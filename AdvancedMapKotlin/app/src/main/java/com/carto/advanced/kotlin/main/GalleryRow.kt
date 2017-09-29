package com.carto.advanced.kotlin.main

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import com.carto.advanced.kotlin.model.Sample
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.utils.Colors

/**
 * Created by aareundo on 30/06/2017.
 */
class GalleryRow(context: Context, sample: Sample) : BaseView(context) {

    var image: ImageView? = null
    var title: TextView? = null
    var description: TextView? = null

    var sample: Sample? = sample

    init {

        val color = Color.WHITE

        if (isJellybeanOrHigher()) {
            background = ColorDrawable(color)
        } else {
            setBackgroundColor(color)
        }

        image = ImageView(context)
        image?.scaleType = ImageView.ScaleType.CENTER_CROP

        addView(image)

        title = TextView(context)
        title?.setTextColor(Colors.appleBlueInt)
        title?.textSize = 14f
        title?.typeface = Typeface.DEFAULT_BOLD
        addView(title)

        description = TextView(context)
        description?.setTextColor(Colors.darkGrayInt)
        description?.textSize = 12f
        addView(description)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 5.0F
        }

        update(sample)
    }

    fun update(sample: Sample) {

        this.sample = sample

        title?.text = this.sample?.title
        title?.measure(0, 0)

        description?.text = this.sample?.description

        image?.setImageResource(this.sample?.imageResource!!)

        layoutSubviews()
    }

    override fun layoutSubviews() {

        val padding: Int = 5
        val imageHeight: Int = frame.height / 5 * 3

        val x: Int = padding
        var y: Int = padding
        val w: Int = frame.width - 2 * padding
        var h: Int = imageHeight

        image?.setFrame(x, y, w, h)

        y += h + padding
        h = title?.measuredHeight!!

        title?.setFrame(x, y, w, h)

        y += h + padding
        h = frame.height - (imageHeight + h + 2 * padding)

        description?.setFrame(x, y, w, h)
    }
}