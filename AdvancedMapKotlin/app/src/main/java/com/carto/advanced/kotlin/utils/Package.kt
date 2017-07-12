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

    fun isGroup(): Boolean {
        return status == null && info == null
    }

    fun isSmallerThan1MB(): Boolean {
        return info?.size?.toLong()!! < (1024 * 1024).toLong()
    }

    fun getStatusText(): String {

        if (info == null) {
            return ""
        }

        var status = "Available"

        val version = info?.version.toString()

        if (isSmallerThan1MB()) {
            status += " v.$version(<1MB)"
        } else {
            val size = (info?.size?.toLong()?.div(1024)?.div(1024)).toString()
            status += " v." + version + " (" + size + "MB)"
        }

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


    companion object {
        val READY = "READY"
        val QUEUED = "QUEUED"
        val ACTION_PAUSE = "PAUSE"
        val ACTION_RESUME = "RESUME"
        val ACTION_CANCEL = "CANCEL"
        val ACTION_DOWNLOAD = "DOWNLOAD"
        val ACTION_REMOVE = "REMOVE"
    }

    fun getActionText(): String {

        if (info == null) {
            return "NONE"
        }

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