package com.carto.advanced.kotlin.sections.base.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.carto.advanced.kotlin.main.MainActivity

/**
 * Created by aareundo on 03/07/2017.
 */

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = intent.getStringExtra(MainActivity.TITLE)
    }
}