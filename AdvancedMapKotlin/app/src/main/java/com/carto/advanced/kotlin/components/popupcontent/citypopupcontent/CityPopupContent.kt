package com.carto.advanced.kotlin.components.popupcontent.citypopupcontent

import android.content.Context
import android.widget.ListView
import com.carto.advanced.kotlin.model.City
import com.carto.advanced.kotlin.sections.base.BaseView
import com.carto.advanced.kotlin.sections.base.setFrame

/**
 * Created by aareundo on 13/07/2017.
 */
class CityPopupContent(context: Context) : BaseView(context) {

    val list = ListView(context)
    val adapter = CityAdapter(context, -1)

    init {
        list.adapter = adapter

        addView(list)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        adapter.width = frame.width
        list.setFrame(0, 0, frame.width, frame.height)
    }

    fun addItems(cities: MutableList<City>) {
        adapter.cities = cities
        adapter.notifyDataSetChanged()
    }

    fun update(item: City) {
        findItem(item.name!!)?.update(item)
    }

    fun findItem(name: String): CityCell? {
        for (i in 0..list.childCount - 1) {
            val child = list.getChildAt(i)

            if (child is CityCell) {
                if (child.item?.name == name) {
                    return child
                }
            }
        }

        return null
    }
}
