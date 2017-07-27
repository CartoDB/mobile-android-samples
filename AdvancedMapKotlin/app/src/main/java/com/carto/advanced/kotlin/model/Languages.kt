package com.carto.advanced.kotlin.model

/**
 * Created by aareundo on 14/07/2017.
 */
class Languages {

    companion object {
        val list: MutableList<Language> = mutableListOf(
                Language("Local", ""),
                Language("English", "en"),
                Language("German", "de"),
                Language("Spanish", "es"),
                Language("Italian", "it"),
                Language("French", "fr"),
                Language("Russian", "ru")
        )
    }
}