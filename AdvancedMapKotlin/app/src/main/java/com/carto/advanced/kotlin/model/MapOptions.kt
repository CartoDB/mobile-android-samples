package com.carto.advanced.kotlin.model

/**
 * Created by aareundo on 14/07/2017.
 */
class MapOptions {

    companion object {
        val list: MutableList<MapOption> = mutableListOf(
                MapOption("Globe view", "globe", false),
                MapOption("3D buildings", "buildings3d", false),
                MapOption("3D texts", "texts3d", true)
        )
    }
}