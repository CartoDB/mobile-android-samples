package com.carto.advanced.kotlin.main

import android.content.Context
import android.graphics.Color
import com.carto.advanced.kotlin.model.Sample
import com.carto.advanced.kotlin.sections.base.BaseScrollView
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.CGRect

/**
 * Created by aareundo on 30/06/2017.
 */
class MainView(context: Context) : BaseView(context) {

    var container: BaseScrollView? = null

    init {
        container = BaseScrollView(context)
        container?.matchParent()
        addView(container)
        frame = CGRect(0, 0, context.resources.displayMetrics.widthPixels, context.resources.displayMetrics.heightPixels)
    }

    override fun layoutSubviews() {

        var itemsInRow: Int = 2

        // TODO improve itemsInRow logic based on mobile-dotnet-samples AdvancedMap.Droid
        if (frame.width > frame.height) {
            itemsInRow = 3

            if (frame.width > 1024) {
                itemsInRow = 4
            }
        } else if (frame.width > 720) {
            itemsInRow = 3
        }

        val padding: Int = (5 * context.resources.displayMetrics.density).toInt()

        var x = padding
        var y = padding
        var w = (frame.width - (itemsInRow + 1) * padding) / itemsInRow
        val h = w

        for (view in views) {

            view.setFrame(x, y, w, h)

            if (x == ((w * (itemsInRow - 1)) + padding * itemsInRow))
            {
                y += h + padding
                x = padding
            }
            else
            {
                x += w + padding
            }
        }
    }

    val views: MutableList<GalleryRow> = mutableListOf()

    fun addRows(samples: List<Sample>) {

        for (i in 0..views.size - 1) {
            container?.removeView(views[i])
        }

        views.clear()

        for (sample in samples) {

            val view = GalleryRow(context)
            view.setBackgroundColor(Color.GREEN)

            views.add(view)
            container?.addView(view)
        }

        layoutSubviews()
    }
}