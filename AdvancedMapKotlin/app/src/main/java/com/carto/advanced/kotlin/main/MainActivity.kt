package com.carto.advanced.kotlin.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carto.advanced.kotlin.model.Samples

class MainActivity : AppCompatActivity() {

    var contentView: MainView? = null

    companion object {
        @JvmField val TITLE = "ACTIVITY_TITLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = MainView(this)
        setContentView(contentView)

        contentView?.addRows(Samples.list)
    }

    override fun onResume() {
        super.onResume()

        for (row in contentView?.views!!) {
            row.setOnClickListener {
                val intent = Intent(this, row.sample?.activity)
                intent.putExtra(TITLE, row.sample?.title)
                startActivity(intent)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        for (row in contentView?.views!!) {
            row.setOnClickListener(null)
        }
    }
}
