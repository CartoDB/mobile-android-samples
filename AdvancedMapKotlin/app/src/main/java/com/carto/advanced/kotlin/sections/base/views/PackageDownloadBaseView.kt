package com.carto.advanced.kotlin.sections.base.views

import android.app.Activity
import android.content.Context
import android.view.View
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent.PackagePopupContent
import com.carto.advanced.kotlin.model.Cities
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity
import com.carto.advanced.kotlin.utils.Package
import com.carto.advanced.kotlin.utils.toList
import com.carto.packagemanager.PackageStatus
import org.jetbrains.anko.doAsync

/**
 * Created by aareundo on 12/07/2017.
 */
open class PackageDownloadBaseView(context: Context, withBaseLayer: Boolean = true) : DownloadBaseView(context, withBaseLayer) {

    var countryButton: PopupButton? = null

    var packageContent: PackagePopupContent? = null

    var folder = ""

    init {

        countryButton = PopupButton(context, R.drawable.icon_global)
        addButton(countryButton!!)

        packageContent = PackagePopupContent(context)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    override fun addListeners() {
        super.addListeners()

        countryButton?.setOnClickListener {
            popup.setPopupContent(packageContent!!)
            popup.popup.header.setText("SELECT A PACKAGE")
            popup.show()
        }

        popup.popup.header.setOnClickListener {
            // Empty listener to catch touches so popup wouldn't close
        }

        popup.popup.header.backButton.setOnClickListener {
            onPopupBackButtonClick()
        }
    }

    override fun removeListeners() {
        super.removeListeners()

        countryButton?.setOnClickListener(null)
    }

    fun updatePackages(packages: MutableList<Package>) {
        packageContent?.addPackages(packages)
    }

    fun getActivity(): BaseActivity {
        return context as BaseActivity
    }

    fun onPackageClick(item: Package) {

        if (item.isGroup()) {
            doAsync {
                folder = folder + item.name + "/"
                val packages = getPackages()

                (context as BaseActivity).runOnUiThread {
                    packageContent?.addPackages(packages)
                    popup.popup.header.backButton.visibility = View.VISIBLE
                }
            }

        } else {

            doAsync {
                val action = item.getActionText()
                enqueue(item)

                if (action == Package.ACTION_DOWNLOAD) {
                    manager?.startPackageDownload(item.id)
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

            getActivity().runOnUiThread {
                progressLabel.show()
            }

        }
    }

    fun onStatusChanged(id: String, status: PackageStatus) {
        (context as Activity).runOnUiThread {

            packageContent?.findAndUpdate(id, status)

            // Bottom label update:
            // As getting the name of a package is a costly operation,
            // only do it when the popup isn't visible
            if (!popup.isVisible()) {
                getCurrentDownload({ download: Package? ->
                    if (download != null) {
                        val progress = status.progress.toInt().toString()
                        val text = "Downloading " + download.name + ": " + progress + "%"
                        (context as Activity).runOnUiThread {
                            progressLabel.update(text)
                            progressLabel.updateProgressBar(status.progress)
                            download.status = status
                        }
                    }
                })
            }
        }

    }

    fun downloadComplete(id: String) {
        (context as Activity).runOnUiThread {
            packageContent!!.addPackages(getPackages())
            packageContent!!.adapter.notifyDataSetChanged()
            dequeue(id)
        }
    }

    fun onPopupBackButtonClick() {
        folder = folder.substring(0, folder.length - 1)

        val lastSlash = folder.lastIndexOf("/")

        if (lastSlash == -1) {
            folder = ""
            popup.popup.header.backButton.visibility = View.GONE
        } else {
            folder = folder.substring(0, lastSlash + 1)
        }

        packageContent?.addPackages(getPackages())
    }

    fun getPackages(): MutableList<Package> {

        val list = mutableListOf<Package>()

        val vector = manager?.serverPackages
        val count = vector!!.size().toInt()

        if (folder == Package.CUSTOM_REGION_FOLDER_NAME + "/") {
            val custom = getCustomRegionPackages()
            for (item in custom) {
                list.add(item)
            }
            return  list
        }

        // Map package download screen's first folder features custom region packages (cities)
        if (folder.isEmpty()) {
            list.add(getCustomRegionFolder())
        }

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

    fun getCustomRegionFolder(): Package {
        val item = Package()
        item.name = Package.CUSTOM_REGION_FOLDER_NAME
        item.id = "NONE"
        return item
    }

    fun getCustomRegionPackages(): MutableList<Package> {
        val items = mutableListOf<Package>()

        for (city in Cities.list) {
            val item = Package()
            item.id = city.bbox.toString()
            item.name = city.name
            item.status = manager?.getLocalPackageStatus(item.id, -1)
            items.add(item)
        }

        return items
    }

    /*
     * Download queue
     */

    var downloadQueue = mutableListOf<Package>()

    fun getCurrentDownload(complete: (item: Package?) -> Unit) {
        doAsync {
            if (downloadQueue.size > 0) {
                val downloading = downloadQueue.filter({ item: Package -> item.isDownloading() })
                if (downloading.size == 1) {
                    complete(downloading[0])
                    return@doAsync
                }
            }

            downloadQueue = getAllPackages().filter({ item: Package -> item.isDownloading() || item.isQueued() }) as MutableList<Package>

            if (downloadQueue.size > 0) {
                val downloading = downloadQueue.filter({ item: Package -> item.isDownloading() })
                if (downloading.size == 1) {
                    complete(downloading[0])
                    return@doAsync
                }
            }

            complete(null)
        }
    }

    fun enqueue(item: Package) {
        if (!downloadQueue.contains(item)) {
            downloadQueue.add(item)
        }
    }

    fun dequeue() {
        downloadQueue.removeAt(0)
    }

    fun dequeue(item: Package) {
        downloadQueue.remove(item)
    }

    fun dequeue(id: String) {
        val item = downloadQueue.find { p: Package -> p.id == id }
        if (item != null) {
            dequeue(item)
        }
    }

    fun getAllPackages(): MutableList<Package> {

        val packages = mutableListOf<Package>()

        val vector = manager!!.serverPackages
        val total = vector.size()

        for (i in 0..total -1) {

            val info = vector?.get(i.toInt())
            val name = info?.name

            val split = name!!.split("/")

            if (split.isEmpty()) {
                continue
            }

            val modified = split[split.size - 1]

            val item = Package()
            item.id = info.packageId
            item.name = modified
            item.status = manager!!.getLocalPackageStatus(item.id, -1)
            item.info = info

            packages.add(item)
        }

        return packages
    }


    fun showLocalPackages() {
        var text = "You have downloaded "

        val packages = getLocalPackages()
        val total = packages.size
        var counter = 0

        for (item in packages) {
            val split = item.name.split("/")
            val shortName = split[split.size - 1]

            text += shortName
            counter++

            if (counter < total) {
                text += ", "
            }
        }

        progressLabel.complete(text)
    }

    fun hasLocalPackages(): Boolean {
        return getLocalPackages().size > 0
    }

    fun getLocalPackages(): MutableList<com.carto.packagemanager.PackageInfo> {
        return manager!!.localPackages.toList()
    }
}