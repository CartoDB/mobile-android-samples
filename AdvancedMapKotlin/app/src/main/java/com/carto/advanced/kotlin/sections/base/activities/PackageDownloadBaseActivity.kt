package com.carto.advanced.kotlin.sections.base.activities

import android.os.Bundle
import com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent.PackageCell
import com.carto.advanced.kotlin.sections.base.views.PackageDownloadBaseView
import com.carto.packagemanager.PackageManagerListener
import com.carto.packagemanager.PackageStatus

/**
 * Created by aareundo on 17/08/2017.
 */
open class PackageDownloadBaseActivity : BaseActivity() {

    var contentView: PackageDownloadBaseView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.manager?.packageManagerListener = object: PackageManagerListener() {
            override fun onPackageListUpdated() {
                val packages = contentView?.getPackages()!!
                runOnUiThread {
                    contentView?.updatePackages(packages)
                }
            }

            override fun onPackageStatusChanged(id: String?, version: Int, status: PackageStatus?) {
                runOnUiThread {
                    contentView?.onStatusChanged(id!!, status!!)
                }
            }

            override fun onPackageUpdated(id: String?, version: Int) {
                runOnUiThread {
                    contentView?.downloadComplete(id!!)
                    packageDownloadComplete()
                }
            }
        }

        contentView?.packageContent?.list?.setOnItemClickListener { _, view, _, _ ->
            run {
                val cell = view as PackageCell
                contentView?.onPackageClick(cell.item!!)
            }
        }

        contentView?.manager?.start()
        contentView?.manager?.startPackageListDownload()

        contentView?.switchButton?.setOnClickListener {
            val isChecked = contentView?.switchButton?.isOnline!!
            if (isChecked) {
                setOnlineMode()
            } else {
                setOfflineMode()
            }
        }

    }

    override fun onPause() {
        super.onPause()

        contentView?.removeListeners()

        contentView?.manager?.packageManagerListener = null

        contentView?.packageContent?.list?.onItemClickListener = null

        contentView?.manager?.stop(false)

        contentView?.switchButton?.setOnClickListener(null)
    }

    open fun packageDownloadComplete() {}

    open fun setOnlineMode() { }

    open fun setOfflineMode() {}
}