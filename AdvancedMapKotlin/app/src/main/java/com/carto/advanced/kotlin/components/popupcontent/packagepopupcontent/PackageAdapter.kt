package com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.carto.advanced.kotlin.utils.Package

/**
 * Created by aareundo on 12/07/2017.
 */
class PackageAdapter(context: Context?, resource: Int) : ArrayAdapter<Package>(context, resource) {

    var packages: MutableList<Package> = mutableListOf()

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
        } else {
            cell = convertView as PackageCell
        }

        cell.update(item)

        return cell
    }
}