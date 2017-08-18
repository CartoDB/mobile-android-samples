package com.carto.advanced.kotlin.utils

import com.carto.packagemanager.PackageAction
import com.carto.packagemanager.PackageInfo
import com.carto.packagemanager.PackageStatus
import java.math.BigInteger

/**
 * Created by aareundo on 12/07/2017.
 */
class Package {

    var id: String? = null

    var name: String? = null

    var status: PackageStatus? = null

    var info: PackageInfo? = null

    val isCustomRegionFolder: Boolean
        get() = name == Package.CUSTOM_REGION_FOLDER_NAME

    fun isCustomRegionPackage(): Boolean {
        if (id == null) {
            return false
        }

        return id!!.contains(Package.BBOX_IDENTIFIER)
    }

    fun isGroup(): Boolean {
        // Custom region packages will have status & info == null, but they're not groups
        return status == null && info == null && !isCustomRegionPackage()
    }

    fun isSmallerThan1MB(): Boolean {
        return info?.size?.toLong()!! < (1024 * 1024).toLong()
    }

    fun getStatusText(): String {

        var status = "Available"

        status += getVersionAndSize()

        if (this.status != null) {

            if (this.status?.currentAction == PackageAction.PACKAGE_ACTION_READY) {
                status = "Ready"
            } else if (this.status?.currentAction == PackageAction.PACKAGE_ACTION_WAITING) {
                status = "Queued"
            } else {
                if (this.status?.currentAction == PackageAction.PACKAGE_ACTION_COPYING) {
                    status = "Copying"
                } else if (this.status?.currentAction == PackageAction.PACKAGE_ACTION_DOWNLOADING) {
                    status = "Downloading"
                } else if (this.status?.currentAction == PackageAction.PACKAGE_ACTION_REMOVING) {
                    status = "Removing"
                }

                status += " " + this.status?.progress + "%"
            }
        }

        return status
    }

    fun getVersionAndSize(): String {

        if (isCustomRegionPackage()) {
            return  ""
        }

        val version = info?.version.toString()
        val size = info?.size?.toMB()

        if (isSmallerThan1MB()) {
            return " v.$version (<1MB)"
        }

        return " v.$version ($size MB)"

    }
    fun isDownloading(): Boolean {
        if (status == null) {
            return false
        }

        return status!!.currentAction == PackageAction.PACKAGE_ACTION_DOWNLOADING && !status!!.isPaused
    }

    fun isQueued(): Boolean {
        if (status == null) {
            return false
        }

        return status!!.currentAction == PackageAction.PACKAGE_ACTION_WAITING && !status!!.isPaused
    }

    companion object {
        val ACTION_PAUSE = "PAUSE"
        val ACTION_RESUME = "RESUME"
        val ACTION_CANCEL = "CANCEL"
        val ACTION_DOWNLOAD = "DOWNLOAD"
        val ACTION_REMOVE = "REMOVE"

        val CUSTOM_REGION_FOLDER_NAME = "CUSTOM_REGION"
        val BBOX_IDENTIFIER = "bbox("
    }

    fun getActionText(): String {

        if (status == null) {
            return ACTION_DOWNLOAD
        }

        val action = status?.currentAction!!

        if (action == PackageAction.PACKAGE_ACTION_READY) {
            return ACTION_REMOVE
        } else if (action == PackageAction.PACKAGE_ACTION_WAITING) {
            return ACTION_CANCEL
        }

        if (status?.isPaused!!) {
            return ACTION_RESUME
        } else {
            return ACTION_PAUSE
        }
    }
}

