package com.carto.advanced.kotlin.model

/**
 * Created by aareundo on 13/07/2017.
 */

class Sample() {

    var imageResource: Int? = null

    var title: String? = null

    var description: String? = null

    var activity: Class<*>? = null

    init {

    }

    constructor(imageResource: Int, title: String, description: String, activity: Class<*>) : this() {
        this.imageResource = imageResource
        this.title = title
        this.description = description
        this.activity = activity
    }
}