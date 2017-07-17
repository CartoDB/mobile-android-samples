package com.carto.advanced.kotlin.sections.styles

import android.os.Bundle
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSection
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSectionItem
import com.carto.advanced.kotlin.sections.base.BaseActivity

/**
 * Created by aareundo on 30/06/2017.
 */
class StyleChoiceActivity : BaseActivity() {

    var contentView: StyleChoiceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = StyleChoiceView(this)
        setContentView(contentView)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.baseMapContent?.getItems()?.forEach {
            section: StylePopupContentSection ->
            section.list.forEach {
                item: StylePopupContentSectionItem ->
                item.setOnClickListener {
                    contentView?.popup?.hide()
                    contentView?.updateBaseLayer(item.label.text as String, section.source!!)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.baseMapContent?.getItems()?.forEach {
            section: StylePopupContentSection ->
            section.list.forEach {
                item: StylePopupContentSectionItem ->
                item.setOnClickListener(null)
            }
        }
    }
}