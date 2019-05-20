package com.carto.advancedmap.kotlinui.mapoptioncontent

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import com.carto.advanced.kotlin.components.popupcontent.mapoptioncontent.MapOptionCell
import com.carto.advanced.kotlin.sections.base.utils.CGRect
import com.carto.advancedmap.model.MapOption

/**
 * Created by aareundo on 14/07/2017.
 */
class MapOptionAdapter(context: Context?, resource: Int) : ArrayAdapter<MapOption>(context, resource) {

    var mapOptions: MutableList<MapOption> = mutableListOf()

    var width: Int = 0

    override fun getCount(): Int {
        return mapOptions.size
    }

    override fun getItem(position: Int): MapOption {
        return mapOptions[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val cell: MapOptionCell?

        val item = mapOptions[position]

        if (convertView == null) {
            cell = MapOptionCell(context)

            val height = (40 * context.resources.displayMetrics.density).toInt()
            cell.layoutParams = AbsListView.LayoutParams(width, height)
            cell.frame = CGRect(0, 0, width, height)
        } else {
            cell = convertView as MapOptionCell
        }

        cell.update(item)
        cell.layoutSubviews()

        return cell
    }
}