package com.carto.advanced.kotlin.sections.geocoding

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import com.carto.advanced.kotlin.sections.base.utils.CGRect
import com.carto.geocoding.GeocodingResult

/**
 * Created by aareundo on 20/07/2017.
 */
class GeocodingResultAdapter(context: Context?) : ArrayAdapter<GeocodingResult>(context, -1) {

    var items: MutableList<GeocodingResult> = mutableListOf()

    var width: Int = 0

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): GeocodingResult {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val cell: GeocodingResultCell?

        val item = items[position]

        if (convertView == null) {
            cell = GeocodingResultCell(context)

            val height = (40 * context.resources.displayMetrics.density).toInt()
            cell.layoutParams = AbsListView.LayoutParams(width, height)
            cell.frame = CGRect(0, 0, width, height)
        } else {
            cell = convertView as GeocodingResultCell
        }

        cell.update(item)
        cell.layoutSubviews()

        return cell
    }
}