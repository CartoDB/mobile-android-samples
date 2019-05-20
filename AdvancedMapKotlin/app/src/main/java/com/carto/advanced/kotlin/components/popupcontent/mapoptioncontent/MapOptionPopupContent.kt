package com.carto.advancedmap.kotlinui.mapoptioncontent

import android.content.Context
import android.widget.ListView
import com.carto.advanced.kotlin.components.popupcontent.mapoptioncontent.MapOptionCell
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advanced.kotlin.model.MapOption

/**
 * Created by aareundo on 14/07/2017.
 */
class MapOptionPopupContent(context: Context) : BaseView(context) {

    val list = ListView(context)
    val adapter = MapOptionAdapter(context, -1)

    init {
        list.adapter = adapter

        addView(list)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        adapter.width = frame.width
        list.setFrame(0, 0, frame.width, frame.height)
    }

    fun addItems(mapOptions: MutableList<MapOption>) {
        adapter.mapOptions = mapOptions
        adapter.notifyDataSetChanged()
    }

    fun update(item: MapOption) {
        findItem(item.name)?.update(item)
    }

    fun findItem(name: String): MapOptionCell? {
        for (i in 0..list.childCount - 1) {
            val child = list.getChildAt(i)

            if (child is MapOptionCell) {
                if (child.item?.name == name) {
                    return child
                }
            }
        }

        return null
    }
}
