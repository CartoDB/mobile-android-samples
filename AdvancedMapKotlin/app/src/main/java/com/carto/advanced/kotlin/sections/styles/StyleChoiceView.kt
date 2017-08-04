package com.carto.advanced.kotlin.sections.styles

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent.LanguagePopupContent
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContent
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.MapBaseView
import com.carto.datasources.CartoOnlineTileDataSource
import com.carto.datasources.HTTPTileDataSource
import com.carto.layers.*
import com.carto.styles.CompiledStyleSet
import com.carto.utils.AssetUtils
import com.carto.utils.ZippedAssetPackage
import com.carto.vectortiles.MBVectorTileDecoder

/**
 * Created by aareundo on 03/07/2017.
 */
class StyleChoiceView(context: Context) : MapBaseView(context) {

    var languageButton: PopupButton = PopupButton(context, R.drawable.icon_language)
    var baseMapButton: PopupButton = PopupButton(context, R.drawable.icon_basemap)

    var languageContent: LanguagePopupContent = LanguagePopupContent(context)
    var baseMapContent: StylePopupContent = StylePopupContent(context)

    var currentLanguage: String = ""
    var currentLayer: TileLayer? = null

    init {

        title = Texts.basemapInfoHeader
        description = Texts.basemapInfoContainer

        currentLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)

        addButton(languageButton)
        addButton(baseMapButton)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    override fun addListeners() {
        super.addListeners()

        languageButton.setOnClickListener {
            popup.setPopupContent(languageContent)
            popup.popup.header.setText("SELECT A LANGUAGE")
            popup.show()
        }

        baseMapButton.setOnClickListener {
            popup.setPopupContent(baseMapContent)
            popup.popup.header.setText("SELECT A BASEMAP")
            popup.show()
        }
    }

    override fun removeListeners() {
        super.removeListeners()

        languageButton.setOnClickListener(null)
        baseMapButton.setOnClickListener(null)
    }


    fun updateMapLanguage(language: String) {

        if (currentLayer == null) {
            return
        }

        currentLanguage = language

        val decoder = (currentLayer as? VectorTileLayer)?.tileDecoder as? MBVectorTileDecoder
        decoder?.setStyleParameter("lang", currentLanguage)
    }

    fun updateBaseLayer(selection: String, source: String) {

        if (source == StylePopupContent.CartoVectorSource) {

            if (selection == StylePopupContent.Voyager) {
                currentLayer = CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)
            } else if (selection == StylePopupContent.Positron) {
                currentLayer = CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_POSITRON)
            } else if (selection == StylePopupContent.DarkMatter) {
                currentLayer = CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARKMATTER)
            }

        } else if (source == StylePopupContent.MapzenSource) {

            val asset = AssetUtils.loadAsset("styles_mapzen.zip")

            val assetPackage = ZippedAssetPackage(asset)

            var name = ""

            if (selection == StylePopupContent.Bright) {
                name = "style"
            } else if (selection == StylePopupContent.Positron) {
                name = "positron"
            } else if (selection == StylePopupContent.DarkMatter) {
                name = "positron_dark"
            }

            val styleSet = CompiledStyleSet(assetPackage, name)

            val datasource = CartoOnlineTileDataSource(source)
            val decoder = MBVectorTileDecoder(styleSet)

            currentLayer = VectorTileLayer(datasource, decoder)

        } else if (source == StylePopupContent.CartoRasterSource) {

            // We know that the value of raster will be Positron or Darkmatter,
            var url: String

            if (selection == StylePopupContent.Positron) {
                url = StylePopupContent.PositronUrl
            } else {
                url = StylePopupContent.DarkMatterUrl
            }

            val datasource = HTTPTileDataSource(1, 19, url)
            currentLayer = RasterTileLayer(datasource)
        }

        if (source == StylePopupContent.CartoRasterSource) {
            languageButton.disable()
        } else {
            languageButton.enable()
        }

        map.layers.clear()
        map.layers.add(currentLayer)

        updateMapLanguage(currentLanguage)
    }

}