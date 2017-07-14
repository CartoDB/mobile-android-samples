package com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import com.carto.advanced.kotlin.model.Language
import com.carto.advanced.kotlin.sections.base.CGRect

/**
 * Created by aareundo on 14/07/2017.
 */
class LanguageAdapter(context: Context?, resource: Int) : ArrayAdapter<Language>(context, resource) {

    var languages: MutableList<Language> = mutableListOf()

    var width: Int = 0

    override fun getCount(): Int {
        return languages.size
    }

    override fun getItem(position: Int): Language {
        return languages[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val cell: LanguageCell?

        val item = languages[position]

        if (convertView == null) {
            cell = LanguageCell(context)

            val height = (40 * context.resources.displayMetrics.density).toInt()
            cell.layoutParams = AbsListView.LayoutParams(width, height)
            cell.frame = CGRect(0, 0, width, height)
        } else {
            cell = convertView as LanguageCell
        }

        cell.update(item)
        cell.layoutSubviews()

        return cell
    }
}