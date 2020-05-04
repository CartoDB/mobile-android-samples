package com.carto.advanced.kotlin.sections.styles

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent.LanguagePopupContent
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContent
import com.carto.advanced.kotlin.components.popupcontent.switchescontent.Switches3DContent
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.MapBaseView
import com.carto.advancedmap.kotlinui.mapoptioncontent.MapOptionPopupContent
import com.carto.components.RenderProjectionMode
import com.carto.datasources.CartoOnlineTileDataSource
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.*
import com.carto.styles.CompiledStyleSet
import com.carto.utils.AssetUtils
import com.carto.utils.ZippedAssetPackage
import com.carto.vectortiles.MBVectorTileDecoder

/**
 * Created by aareundo on 03/07/2017.
 */
class StyleChoiceView(context: Context) : MapBaseView(context) {

    companion object {
        // Content descriptions for auto tests
        val LANGUAGE_BUTTON_DESCRIPTION = "language_button"
        val BASEMAP_BUTTON_DESCRIPTION = "basemap_button"
        val STYLE_POSITRON_DESCRIPTION = "style_positron"
    }

    var languageButton = PopupButton(context, R.drawable.icon_language)
    var baseMapButton = PopupButton(context, R.drawable.icon_basemap)
    var mapOptionButton = PopupButton(context, R.drawable.icon_switches)

    var languageContent = LanguagePopupContent(context)
    var baseMapContent = StylePopupContent(context)
    var mapOptionContent = MapOptionPopupContent(context)

    var currentLanguage: String = ""
    var currentLayer: TileLayer? = null
    var buildings3D = false
    var texts3D = true
    var pois = false

    private val vectorSource = LocalVectorDataSource(projection)
    private val vectorLayer = VectorLayer(vectorSource)
    private val clickListener = VectorTileListener(vectorLayer)

    init {

        title = Texts.basemapInfoHeader
        description = Texts.basemapInfoContainer

        currentLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)

        map.layers.add(vectorLayer)
        (currentLayer as VectorTileLayer).vectorTileEventListener = clickListener

        addButton(languageButton)
        addButton(baseMapButton)
        addButton(mapOptionButton)

        layoutSubviews()

        languageButton.contentDescription = LANGUAGE_BUTTON_DESCRIPTION
        baseMapButton.contentDescription = BASEMAP_BUTTON_DESCRIPTION

        baseMapContent.cartoVector.list[1].contentDescription = STYLE_POSITRON_DESCRIPTION
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

        mapOptionButton.setOnClickListener {
            popup.setPopupContent(mapOptionContent)
            popup.popup.header.setText("CONFIGURE RENDERING")
            popup.show()
        }
    }

    override fun removeListeners() {
        super.removeListeners()

        languageButton.setOnClickListener(null)
        baseMapButton.setOnClickListener(null)
        mapOptionButton.setOnClickListener(null)
    }

    fun updateMapLanguage(language: String) {
        currentLanguage = language
        
        if (currentLayer is CartoOnlineVectorTileLayer) {
            (currentLayer as CartoOnlineVectorTileLayer).language = language
            (currentLayer as CartoOnlineVectorTileLayer).fallbackLanguage = ""
        }
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

        } else if (source == StylePopupContent.CartoRasterSource) {

            if (selection == StylePopupContent.HereSatelliteDaySource) {
                currentLayer = CartoOnlineRasterTileLayer("here.satellite.day@2x")
            } else if (selection == StylePopupContent.HereNormalDaySource) {
                currentLayer = CartoOnlineRasterTileLayer("here.normal.day@2x")
            }
        }

        if (source == StylePopupContent.CartoRasterSource) {
            languageButton.disable()
        } else {
            languageButton.enable()
        }

        map.layers.clear()
        map.layers.add(currentLayer)

        updateMapLanguage(currentLanguage)
        updateMapOption("buildings3d", buildings3D)
        updateMapOption("texts3d", texts3D)
        updateMapOption("pois", pois)

        if (currentLayer is VectorTileLayer) {
            map.layers.add(vectorLayer)
            (currentLayer as VectorTileLayer).vectorTileEventListener = clickListener
        }
    }

    fun updateMapOption(option: String, value: Boolean) {
        if (option == "globe") {
            map.options.renderProjectionMode = if (value) RenderProjectionMode.RENDER_PROJECTION_MODE_SPHERICAL else RenderProjectionMode.RENDER_PROJECTION_MODE_PLANAR
            return
        }
        if (option == "buildings3d") {
            buildings3D = value
        }
        if (option == "texts3d") {
            texts3D = value
        }
        if (option == "pois") {
            pois = value
        }

        if (!(currentLayer is VectorTileLayer)) {
            return
        }

        val decoder = (currentLayer as? VectorTileLayer)?.tileDecoder as? MBVectorTileDecoder
        if (option == "buildings3d") {
            decoder?.setStyleParameter("buildings", if (buildings3D) "2" else "1")
        }
        if (option == "texts3d") {
            decoder?.setStyleParameter("texts3d", if (texts3D) "1" else "0")
        }
        if (option == "pois" && currentLayer is CartoVectorTileLayer) {
            val cartoLayer = currentLayer as? CartoVectorTileLayer
            cartoLayer?.poiRenderMode = if (pois) CartoBaseMapPOIRenderMode.CARTO_BASEMAP_POI_RENDER_MODE_FULL else CartoBaseMapPOIRenderMode.CARTO_BASEMAP_POI_RENDER_MODE_NONE
        }
    }

    private fun updateStyle(key: String, value: String) {
        val decoder = (currentLayer as? VectorTileLayer)?.tileDecoder as? MBVectorTileDecoder
        decoder?.setStyleParameter(key, value)
    }

}