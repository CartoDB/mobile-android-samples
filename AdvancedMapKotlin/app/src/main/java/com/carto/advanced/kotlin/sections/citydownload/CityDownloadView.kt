package com.carto.advanced.kotlin.sections.citydownload

import android.app.Activity
import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.citypopupcontent.CityPopupContent
import com.carto.advanced.kotlin.model.Cities
import com.carto.advanced.kotlin.model.City
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.DownloadBaseView
import com.carto.advanced.kotlin.utils.toMB
import com.carto.packagemanager.PackageStatus

/**
 * Created by aareundo on 03/07/2017.
 */
class CityDownloadView(context: Context) : DownloadBaseView(context) {

    var cityButton = PopupButton(context, R.drawable.icon_global)

    var cityContent = CityPopupContent(context)

    init {

        title = Texts.cityDownloadInfoHeader
        description = Texts.cityDownloadInfoContainer

        addButton(cityButton)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    override fun addListeners() {
        super.addListeners()

        cityButton.setOnClickListener {
            popup.setPopupContent(cityContent)
            popup.popup.header.setText("DOWNLOAD A CITY")
            popup.show()
        }
    }

    override fun removeListeners() {
        super.removeListeners()

        cityButton.setOnClickListener(null)
    }

    fun getCurrentDownload(id: String): City? {
        return Cities.list.find { city: City -> city.bbox.toString() == id }
    }

    fun onStatusChanged(id: String, status: PackageStatus) {
        (context as Activity).runOnUiThread {

            val progress = status.progress.toInt().toString()
            val name = getCurrentDownload(id)?.name
            progressLabel.update("Downloading $name: $progress%")
            progressLabel.updateProgressBar(status.progress)
        }
    }

    fun downloadComplete(id: String) {
        (context as Activity).runOnUiThread {
            val current = getCurrentDownload(id)

            val size = manager?.getLocalPackage(current?.bbox.toString())?.size!!
            getCurrentDownload(id)?.size = size.toMB()

            cityContent.update(current!!)

            val name = current.name.toUpperCase()
            progressLabel.complete("DOWNLOAD OF " + name + " COMPLETE (" + size.toMB() + " MB)")

            val position = projection?.fromWgs84(current.bbox.center)
            map.setFocusPos(position, 1.0f)
            map.setZoom(8.0f, 1.0f)
        }
    }

    fun onCityClick(city: City) {

        if (city.existsLocally) {
            downloadComplete(city.bbox.toString())
            return
        }

        progressLabel.show()
        progressLabel.update("STARTING DOWNLOAD OF " + city.name)

        popup.hide()
    }
}