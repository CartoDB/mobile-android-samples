package com.carto.advanced.kotlin.components.popupcontent.citypopupcontent

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import com.carto.advanced.kotlin.model.City
import com.carto.advanced.kotlin.sections.base.utils.CGRect

/**
 * Created by aareundo on 13/07/2017.
 */

class CityAdapter(context: Context?, resource: Int) : ArrayAdapter<City>(context, resource) {

    var cities: MutableList<City> = mutableListOf()

    var width: Int = 0

    override fun getCount(): Int {
        return cities.size
    }

    override fun getItem(position: Int): City {
        return cities[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val cell: CityCell?

        val item = cities[position]

        if (convertView == null) {
            cell = CityCell(context)

            val height = (40 * context.resources.displayMetrics.density).toInt()
            cell.layoutParams = AbsListView.LayoutParams(width, height)
            cell.frame = CGRect(0, 0, width, height)
        } else {
            cell = convertView as CityCell
        }

        cell.update(item)
        cell.layoutSubviews()

        return cell
    }
}