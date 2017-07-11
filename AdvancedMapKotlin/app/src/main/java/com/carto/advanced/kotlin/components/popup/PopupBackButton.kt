package com.carto.advanced.kotlin.components.popup

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame

/**
 * Created by aareundo on 04/07/2017.
 */
class PopupBackButton(context: Context) : BaseView(context) {

    val button = ImageView(context)
    val text = TextView(context)

    init {

//        button.imager
        addView(button)

        text.text = "BACK"
        addView(text)
    }

    override fun layoutSubviews() {

        val padding: Int = (3 * context.resources.displayMetrics.density).toInt()
        val imagePadding: Int = frame.height / 4

        var x: Int = 0
        var y: Int = imagePadding
        var h: Int = frame.height - 2 * imagePadding
        var w: Int = h / 2

        button.setFrame(x, y, w, h)

        x = w + imagePadding
        y = 0
        w = frame.width - (x + padding)
        h = frame.height

        text.setFrame(x, y, w, h)
    }
}