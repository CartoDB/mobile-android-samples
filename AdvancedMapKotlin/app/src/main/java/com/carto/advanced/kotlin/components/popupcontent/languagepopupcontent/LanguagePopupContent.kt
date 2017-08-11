package com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent

import android.content.Context
import android.widget.ListView
import com.carto.advanced.kotlin.model.Language
import com.carto.advanced.kotlin.sections.base.views.BaseView
import com.carto.advanced.kotlin.sections.base.utils.setFrame

/**
 * Created by aareundo on 14/07/2017.
 */
class LanguagePopupContent(context: Context) : BaseView(context) {

    val list = ListView(context)
    val adapter = LanguageAdapter(context, -1)

    init {
        list.adapter = adapter

        addView(list)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        adapter.width = frame.width
        list.setFrame(0, 0, frame.width, frame.height)
    }

    fun addItems(languages: MutableList<Language>) {
        adapter.languages = languages
        adapter.notifyDataSetChanged()
    }

    fun update(item: Language) {
        findItem(item.name)?.update(item)
    }

    fun findItem(name: String): LanguageCell? {
        for (i in 0..list.childCount - 1) {
            val child = list.getChildAt(i)

            if (child is LanguageCell) {
                if (child.item?.name == name) {
                    return child
                }
            }
        }

        return null
    }
}