package com.carto.advanced.kotlin.sections.buildingfloors

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar

class BuildingFloorsActivity : AppCompatActivity() {

    var contentView: BuildingFloorsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = BuildingFloorsView(this)
        setContentView(contentView)

        for (switch in contentView?.switchesContent!!.list) {
            switch.check()
        }

        contentView!!.switchesContent.setProgress(5)
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

        contentView!!.switchesContent.slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                contentView!!.switchesContent.label.text = p1.toString()
                contentView!!.showFloors(p1)
            }

        })
    }

    override fun onPause() {
        super.onPause()

        contentView?.removeListeners()

        for (switch in contentView?.switchesContent!!.list) {
            switch.switch.setOnCheckedChangeListener(null)
        }

        contentView!!.switchesContent.slider.setOnSeekBarChangeListener(null)
    }
}
