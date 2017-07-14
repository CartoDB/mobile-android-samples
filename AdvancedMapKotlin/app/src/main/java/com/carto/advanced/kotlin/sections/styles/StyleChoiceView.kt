package com.carto.advanced.kotlin.sections.styles

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent.LanguagePopupContent
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.TileLayer

/**
 * Created by aareundo on 03/07/2017.
 */
class StyleChoiceView(context: Context) : MapBaseView(context) {

    var languageButton: PopupButton = PopupButton(context, R.drawable.icon_language)
    var baseMapButton: PopupButton = PopupButton(context, R.drawable.icon_basemap)

    var languageContent: LanguagePopupContent = LanguagePopupContent(context)
//    var baseMapContent: StylePopupContent = StylePopupContent(context)

    var currentLanguage: String = ""
    var currentSource: String = "nutiteq.osm"
    var currentLayer: TileLayer? = null

    init {

        title = Texts.basemapInfoHeader
        description = Texts.basemapInfoContainer

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    override fun addListeners() {
        super.addListeners()
    }

    override fun removeListeners() {
        super.removeListeners()
    }

}