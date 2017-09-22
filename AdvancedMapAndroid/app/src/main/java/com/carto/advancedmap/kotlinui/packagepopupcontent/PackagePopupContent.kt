package com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent

import android.content.Context
import android.widget.ListView
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import com.carto.advancedmap.model.Package
import com.carto.packagemanager.PackageStatus

/**
 * Created by aareundo on 12/07/2017.
 */
class PackagePopupContent(context: Context) : BaseView(context) {

    val list = ListView(context)
    val adapter = PackageAdapter(context, -1)

    init {
        list.adapter = adapter

        addView(list)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        adapter.width = frame.width
        list.setFrame(0, 0, frame.width, frame.height)
    }

    fun addPackages(packages: MutableList<Package>) {
        adapter.packages = packages
        adapter.notifyDataSetChanged()
    }

    fun findAndUpdate(item: Package) {
        find(item.id!!)?.update(item)
    }

    fun findAndUpdate(item: Package, progress: Float) {
        find(item.id!!)?.update(item, progress)
    }

    fun findAndUpdate(id: String, status: PackageStatus) {
        find(id)?.update(status)
    }

    fun find(id: String): PackageCell? {
        for (i in 0..list.childCount - 1) {
            val child = list.getChildAt(i)

            if (child is PackageCell) {
                if (child.item?.id == id) {
                    return child
                }
            }
        }

        return null
    }
}