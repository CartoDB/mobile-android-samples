package com.carto.advanced.kotlin.utils

import com.carto.packagemanager.PackageInfo
import com.carto.packagemanager.PackageInfoVector

/**
 * Created by aareundo on 27/07/2017.
 */
fun PackageInfoVector.toList(): MutableList<PackageInfo> {

    val list = mutableListOf<PackageInfo>()

    for (i in 0..size() - 1) {
        list.add(get(i.toInt()))
    }

    return list
}