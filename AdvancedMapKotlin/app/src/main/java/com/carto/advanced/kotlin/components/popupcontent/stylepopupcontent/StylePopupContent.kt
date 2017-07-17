package com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.sections.base.BaseScrollView
import com.carto.advanced.kotlin.sections.base.BaseView
import org.jetbrains.anko.displayMetrics

/**
 * Created by aareundo on 17/07/2017.
 */
class StylePopupContent(context: Context) : BaseView(context) {

    companion object {
        val NutiteqSource = "nutiteq.osm"
        val MapzenSource = "mapzen.osm"
        val CartoSource = "carto.osm"

        val Bright = "BRIGHT"
        val Gray = "GRAY"
        val Dark = "DARK"

        val Positron = "POSITRON"
        val DarkMatter = "DARKMATTER"
        val Voyager = "VOYAGER"

        val PositronUrl = "http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png";
        val DarkMatterUrl = "http://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}.png";
    }

    val container = BaseScrollView(context)

    val cartoVector = StylePopupContentSection(context)
    val mapzen = StylePopupContentSection(context)
    val cartoRaster = StylePopupContentSection(context)

    fun getItems() : MutableList<StylePopupContentSection> {
        return mutableListOf(cartoVector, mapzen, cartoRaster)
    }

    init {

        addView(container)

        cartoVector.source = NutiteqSource
        cartoVector.header.text = "CARTO VECTOR"
        cartoVector.addItem(Bright, R.drawable.style_image_nutiteq_bright)
        cartoVector.addItem(Gray, R.drawable.style_image_nutiteq_gray)
        cartoVector.addItem(Dark, R.drawable.style_image_nutiteq_dark)
        cartoVector.addItem(Positron, R.drawable.style_image_nutiteq_positron)
        cartoVector.addItem(DarkMatter, R.drawable.style_image_nutiteq_darkmatter)
        cartoVector.addItem(Voyager, R.drawable.style_image_nutiteq_voyager)

        container.addView(cartoVector)

        mapzen.source = MapzenSource
        mapzen.header.text = "MAPZEN VECTOR"
        mapzen.addItem(Bright, R.drawable.style_image_mapzen_bright)
        mapzen.addItem(Positron, R.drawable.style_image_mapzen_positron)
        mapzen.addItem(DarkMatter, R.drawable.style_image_mapzen_darkmatter)

        container.addView(mapzen)

        cartoRaster.source = CartoSource
        cartoRaster.header.text = "CARTO RASTER"
        cartoRaster.addItem(StylePopupContent.Positron, R.drawable.style_image_carto_positron)
        cartoRaster.addItem(DarkMatter, R.drawable.style_image_carto_darkmatter)

        container.addView(cartoRaster)
    }

    override fun layoutSubviews() {

        container.setFrame(0, 0, frame.width, frame.height)

        val padding = (5 * context.resources.displayMetrics.density).toInt()
        val headerPadding = (20 * context.resources.displayMetrics.density).toInt()

        val x = padding
        var y = 0
        val w = frame.width - 2 * padding
        var h = cartoVector.getCalculatedHeight()

        cartoVector.setFrame(x, y, w, h)

        y += h + headerPadding
        h = mapzen.getCalculatedHeight()

        mapzen.setFrame(x, y, w, h)

        y += h + headerPadding
        h = cartoRaster.getCalculatedHeight()

        cartoRaster.setFrame(x, y, w, h)
    }

}