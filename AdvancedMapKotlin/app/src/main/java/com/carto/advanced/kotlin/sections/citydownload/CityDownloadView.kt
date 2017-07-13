package com.carto.advanced.kotlin.sections.citydownload

import android.app.Activity
import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.citypopupcontent.CityPopupContent
import com.carto.advanced.kotlin.model.City
import com.carto.advanced.kotlin.sections.base.DownloadBaseView
import com.carto.packagemanager.PackageStatus

/**
 * Created by aareundo on 03/07/2017.
 */
class CityDownloadView(context: Context) : DownloadBaseView(context) {

    var cityButton = PopupButton(context, R.drawable.icon_global)

    var cityContent = CityPopupContent(context)

    init {
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

    var currentDownload: City? = null

    fun onStatusChanged(status: PackageStatus) {
        (context as Activity).runOnUiThread {

            if (this.currentDownload == null) {
                // TODO in case a download has been started and the activity is reloaded
                return@runOnUiThread
            }

            val text = "Downloading " + currentDownload?.name + ": " + status.progress.toString() + "%"
            progressLabel.update(text)
            progressLabel.updateProgressBar(status.progress)
        }
    }

    fun downloadComplete() {
        (context as Activity).runOnUiThread {
            currentDownload?.existsLocally = true
            cityContent.update(currentDownload!!)
            progressLabel.update("DOWNLOAD OF " + currentDownload?.name + " COMPLETE")

            val position = projection?.fromWgs84(currentDownload?.bbox?.center)
            map.setFocusPos(position, 1.0f)
            map.setZoom(8.0f, 1.0f)
        }
    }

    fun onCityClick(city: City) {

        if (city.existsLocally) {
            downloadComplete()
            return
        }

        currentDownload = city
        progressLabel.show()
        progressLabel.update("STARTING DOWNLOAD OF " + city.name)

        popup.hide()
    }
}