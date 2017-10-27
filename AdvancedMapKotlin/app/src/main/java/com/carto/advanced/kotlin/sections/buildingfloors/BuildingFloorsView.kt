package com.carto.advanced.kotlin.sections.buildingfloors

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.switchescontent.SwitchesFloorContent
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.MapBaseView
import com.carto.core.MapPos
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.layers.VectorLayer
import com.carto.utils.AssetUtils
import com.carto.vectorelements.NMLModel

/**
 * Created by aareundo on 25/10/2017.
 */
class BuildingFloorsView(context: Context) : MapBaseView(context) {

    // Count is actually 6 -> 0..5
    private val floorCount = 5

    private var baseLayer: CartoOnlineVectorTileLayer? = null
    var source: LocalVectorDataSource? = null
    var objectLayer: VectorLayer? = null

    var switchesButton = PopupButton(context, R.drawable.icon_switches)
    var switchesContent = SwitchesFloorContent(context, floorCount)

    init {

        title = Texts.vectorElementsInfoHeader
        description = Texts.vectorElementsInfoContainer

        baseLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)

        source = LocalVectorDataSource(projection)
        objectLayer = VectorLayer(source)
        map.layers.add(objectLayer)

        addButton(switchesButton)

        val longitude = -77.0369
        val latitude = 38.9072
        val washingtonDC = projection?.fromWgs84(MapPos(longitude, latitude))

        // Set specific focus, zoom, tilt and rotation for an optimal initial view
        map.setFocusPos(washingtonDC, 0.0f)
        map.setZoom(15.5f, 0.0f)
        map.setTilt(20.0f, 0.0f)
        map.rotate(-30.0f, 0.0f)

        layoutSubviews()

        for (i in 0..floorCount) {
            val data = AssetUtils.loadAsset("3dwf-f$i.nml")
            val model = NMLModel(washingtonDC, data)
            model.scale = 20.0f
            source?.add(model)
        }

        switchesContent.slider.max = floorCount
    }

    override fun addListeners() {
        super.addListeners()

        switchesButton.setOnClickListener {
            popup.setPopupContent(switchesContent)
            popup.popup.header.setText("SLIDE TO SHOW OR HIDE FLOORS")
            popup.show()
        }

        switchesContent.setOnClickListener({
//             Just catches background clicks, so they wouldn't close the popup
        })
    }

    override fun removeListeners() {
        super.removeListeners()

        switchesButton.setOnClickListener(null)

        switchesContent.setOnClickListener(null)
    }

    fun showFloor(i: Int, checked: Boolean) {
        source?.all!![i].isVisible = checked
    }

    fun showFloors(count: Int) {

        val elementCount = source?.all!!.size()

        for (i in 0 until elementCount) {
            source?.all!![i.toInt()].isVisible = false
        }

        for (i in 0 until count + 1) {
            source?.all!![i].isVisible = true
        }
    }
}