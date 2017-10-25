package com.carto.advanced.kotlin.sections.buildingfloors

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class BuildingFloorsActivity : AppCompatActivity() {

    var contentView: BuildingFloorsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = BuildingFloorsView(this)
        setContentView(contentView)

        for (switch in contentView?.switchesContent!!.list) {
            switch.check()
        }
    }

    override fun onResume() {
        super.onResume()

        contentView?.addListeners()

        for (switch in contentView?.switchesContent!!.list) {
            switch.switch.setOnCheckedChangeListener({ _, b ->
                contentView!!.showFloor(switch.id, switch.switch.isChecked)
                contentView!!.popup.hide()
            })
        }
    }

    override fun onPause() {
        super.onPause()

        contentView?.removeListeners()

        for (switch in contentView?.switchesContent!!.list) {
            switch.switch.setOnCheckedChangeListener(null)
        }
    }
}
