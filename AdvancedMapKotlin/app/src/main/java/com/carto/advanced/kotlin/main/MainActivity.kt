package com.carto.advanced.kotlin.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carto.advanced.kotlin.model.Samples

class MainActivity : AppCompatActivity() {

    var contentView: MainView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = MainView(this)
        setContentView(contentView)

        contentView?.addRows(Samples.list)
    }
}
