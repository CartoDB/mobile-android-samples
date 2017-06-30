package com.carto.advanced.kotlin.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carto.advanced.kotlin.model.Samples
import com.carto.advanced.kotlin.sections.base.MapBaseView

class MainActivity : AppCompatActivity() {

    var contentView: MainView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = MainView(this)
        setContentView(contentView)

        contentView?.addRows(Samples.list)
    }
}
