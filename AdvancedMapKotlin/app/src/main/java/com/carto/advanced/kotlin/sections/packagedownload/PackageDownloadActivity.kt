package com.carto.advanced.kotlin.sections.packagedownload

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import com.carto.advanced.kotlin.sections.base.BaseActivity
import com.carto.advanced.kotlin.utils.Utils
import com.carto.packagemanager.CartoPackageManager
import com.carto.packagemanager.PackageManagerListener
import com.carto.packagemanager.PackageStatus

class PackageDownloadActivity : BaseActivity(), AdapterView.OnItemClickListener {

    var contentView: PackageDownloadView? = null

    val MAP_SOURCE = "nutiteq.osm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = PackageDownloadView(this)
        setContentView(contentView)

        val folder = Utils.createDirectory(this, "countrypackages")
        contentView?.manager = CartoPackageManager(MAP_SOURCE, folder)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()
        contentView?.packageContent?.list?.onItemClickListener = this

        contentView?.manager?.packageManagerListener = object: PackageManagerListener() {
            override fun onPackageListUpdated() {
                contentView?.updatePackages()
            }

            override fun onPackageStatusChanged(id: String?, version: Int, status: PackageStatus?) {
                contentView?.onStatusChanged(status!!)
            }

            override fun onPackageUpdated(id: String?, version: Int) {
                contentView?.downloadComplete(id!!)
            }
        }

        contentView?.manager?.start()
        contentView?.manager?.startPackageListDownload()
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.packageContent?.list?.onItemClickListener = null

        contentView?.manager?.packageManagerListener = null

        contentView?.manager?.stop(false)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
