package com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import com.carto.advanced.kotlin.sections.base.CGRect
import com.carto.advanced.kotlin.utils.Package

/**
 * Created by aareundo on 12/07/2017.
 */
class PackageAdapter(context: Context?, resource: Int) : ArrayAdapter<Package>(context, resource) {

    var packages: MutableList<Package> = mutableListOf()

    var width: Int = 0

    override fun getCount(): Int {
        return packages.size
    }

    override fun getItem(position: Int): Package {
        return packages[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val cell: PackageCell?

        val item = packages[position]

        if (convertView == null) {
            cell = PackageCell(context)

            val height = (45 * context.resources.displayMetrics.density).toInt()
            cell.layoutParams = AbsListView.LayoutParams(width, height)
            cell.frame = CGRect(0, 0, width, height)
        } else {
            cell = convertView as PackageCell
        }

        cell.update(item)
        cell.layoutSubviews()

        return cell
    }
}