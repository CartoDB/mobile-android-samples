package com.carto.advanced.kotlin.sections.base

import android.app.Activity
import android.content.Context
import android.view.View
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent.PackagePopupContent
import com.carto.advanced.kotlin.utils.Package
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOfflineVectorTileLayer
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.packagemanager.CartoPackageManager
import com.carto.packagemanager.PackageStatus
import org.jetbrains.anko.collections.toAndroidPair

/**
 * Created by aareundo on 12/07/2017.
 */
open class PackageDownloadBaseView(context: Context) : DownloadBaseView(context) {

    var countryButton: PopupButton? = null

    var packageContent: PackagePopupContent? = null

    var onlineLayer: CartoOnlineVectorTileLayer? = null
    var offlineLayer: CartoOfflineVectorTileLayer? = null

    var currentDownload: com.carto.advanced.kotlin.utils.Package? = null
    var folder = ""

    var manager: CartoPackageManager? = null

    init {
        countryButton = PopupButton(context, R.drawable.icon_global)
        addButton(countryButton!!)

        packageContent = PackagePopupContent(context)
    }

    open override fun layoutSubviews() {
        super.layoutSubviews()
    }

    override fun addListeners() {
        super.addListeners()

        countryButton?.setOnClickListener {
            popup.setPopupContent(packageContent!!)
            popup.popup.header.setText("SELECT A PACKAGE")
            popup.show()
        }
    }

    override fun removeListeners() {
        super.removeListeners()

        countryButton?.setOnClickListener(null)
    }

    fun setOnlineMode() {
        map.layers?.remove(offlineLayer)
        map.layers?.insert(0, onlineLayer)
    }

    fun setOfflineMode(manager: CartoPackageManager) {
        map.layers?.remove(onlineLayer)
        offlineLayer = CartoOfflineVectorTileLayer(manager, CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT)
        map.layers?.insert(0, offlineLayer)
    }

    fun updatePackages() {
        packageContent?.addPackages(getPackages())
    }

    fun setOfflineMode() {
        setOfflineMode(manager!!)
    }

    fun onPackageClick(item: com.carto.advanced.kotlin.utils.Package) {

        if (item.isGroup()) {
            folder = folder + item.name + "/"
            packageContent?.addPackages(getPackages())
            popup.popup.header.backButton.visibility = View.VISIBLE
        } else {

            currentDownload = item
            val action = item.getActionText()

            if (action == Package.ACTION_DOWNLOAD) {
                manager?.startPackageDownload(item.id)
                progressLabel.show()
            } else if (action == Package.ACTION_PAUSE) {
                manager?.setPackagePriority(item.id, -1)
            } else if (action == Package.ACTION_RESUME) {
                manager?.setPackagePriority(item.id, 0)
            } else if (action == Package.ACTION_CANCEL) {
                manager?.cancelPackageTasks(item.id)
            } else if (action == Package.ACTION_REMOVE) {
                manager?.startPackageRemove(item.id)
            }
        }
    }

    fun onStatusChanged(status: PackageStatus) {
        (context as Activity).runOnUiThread {

            if (this.currentDownload == null) {
                // TODO in case a download has been started and the activity is reloaded
                return@runOnUiThread
            }

            val text = "Downloading " + currentDownload?.name + ": " + status.progress.toString()
            progressLabel.update(text)
            progressLabel.updateProgressBar(status.progress)

            currentDownload?.status = manager?.getLocalPackageStatus(currentDownload?.id, -1)
            packageContent?.findAndUpdate(currentDownload!!, status.progress)
        }
    }

    fun downloadComplete(id: String) {
        (context as Activity).runOnUiThread {
            if (currentDownload != null) {
                currentDownload?.status = manager?.getLocalPackageStatus(id, -1)
                packageContent?.findAndUpdate(currentDownload!!)
            }
        }
    }

    fun onPopupBackButtonClick() {
        folder = folder.substring(folder.length - 1)
        val lastSlash = folder.lastIndexOf("/")

        if (lastSlash == -1) {
            folder = ""
            popup.popup.header.backButton.visibility = View.GONE
        } else {
            folder = folder.substring(lastSlash + 1)
        }

        packageContent?.addPackages(getPackages())
    }

    fun getPackages(): MutableList<com.carto.advanced.kotlin.utils.Package> {

        val list = mutableListOf<com.carto.advanced.kotlin.utils.Package>()

        val vector = manager?.serverPackages
        val count = vector!!.size().toInt()

        for (i in 0..count - 1) {
            val info = vector.get(i)
            val name = info?.name

            val item = Package()

            if (!name!!.startsWith(folder)) {
                // Belongs to a different folder,
                // should not be added if name is e.g. Asia/, while folder is /Europe
                continue
            }

            var modified = name.substring(folder.length)
            val index = modified.indexOf("/")

            if (index == -1) {
                // This is an actual package
                item.id = info.packageId
                item.name = modified
                item.status = manager?.getLocalPackageStatus(item.id, -1)
                item.info = info
            } else {
                // This is a package group
                modified = modified.substring(0, index)

                val found = list.filter { p: Package -> p.name == modified }

                if (found.isEmpty()) {
                    // If there are none, add a package group if we don't have an existing list item
                    item.name = modified
                } else if (found.size == 1 && found[0].info != null) {
                    // Sometimes we need to add two labels with the same name.
                    // One a downloadable package and the other pointing to a list of said country's counties,
                    // such as with Spain, Germany, France, Great Britain

                    // If there is one existing package and its info isn't null,
                    // we will add a "parent" package containing subpackages (or package group)
                    item.name = modified
                } else {
                    // Shouldn't be added, as both cases are accounted for
                    continue
                }
            }

            list.add(item)
        }

        return  list
    }
}