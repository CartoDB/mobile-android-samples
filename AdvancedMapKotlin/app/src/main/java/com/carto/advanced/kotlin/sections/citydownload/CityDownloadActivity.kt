package com.carto.advanced.kotlin.sections.citydownload

import android.os.Bundle
import com.carto.advanced.kotlin.components.popupcontent.citypopupcontent.CityCell
import com.carto.advanced.kotlin.model.Cities
import com.carto.advanced.kotlin.sections.base.BaseActivity
import com.carto.advanced.kotlin.utils.Utils
import com.carto.packagemanager.CartoPackageManager
import com.carto.packagemanager.PackageManagerListener
import com.carto.packagemanager.PackageStatus
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

class CityDownloadActivity : BaseActivity() {

    var contentView: CityDownloadView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = CityDownloadView(this)
        setContentView(contentView)

        val folder = Utils.createDirectory(this, "citypackages")
        contentView?.manager = CartoPackageManager(contentView!!.MAP_SOURCE, folder)

        for (city in Cities.list) {
            val id = city.bbox.toString()
            val status = contentView?.manager?.getLocalPackageStatus(id, -1)

            if (status != null && status.progress == 100.0f) {
                city.existsLocally = true
            }
        }

        contentView?.cityContent?.addItems(Cities.list)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.manager?.packageManagerListener = object: PackageManagerListener() {

            override fun onPackageStatusChanged(id: String?, version: Int, status: PackageStatus?) {
                runOnUiThread {
                    contentView?.onStatusChanged(status!!)
                }
            }

            override fun onPackageUpdated(id: String?, version: Int) {
                runOnUiThread {
                    contentView?.downloadComplete()
                }
            }
        }

        contentView?.cityContent?.list?.setOnItemClickListener { _, view, _, _ ->
            run {
                val cell = view as CityCell


                val id = cell.item?.bbox.toString()
                contentView?.manager?.startPackageDownload(id)

                contentView?.onCityClick(cell.item!!)
            }
        }

        contentView?.onlineSwitch?.switch?.onCheckedChange { _, isChecked ->
            run {
                if (isChecked) {
                    contentView?.setOnlineMode()
                } else {
                    contentView?.setOfflineMode()
                }
            } }

        contentView?.manager?.start()
        contentView?.manager?.startPackageListDownload()
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.removeListeners()

        contentView?.cityContent?.list?.onItemClickListener = null

        contentView?.manager?.packageManagerListener = null

        contentView?.onlineSwitch?.switch?.setOnCheckedChangeListener(null)

        contentView?.manager?.stop(false)
    }
}
