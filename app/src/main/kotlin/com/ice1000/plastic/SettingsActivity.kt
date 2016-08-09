package com.ice1000.plastic

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import data.constants.LAYOUT_LIST
import data.constants.LAYOUT_PREFERENCE
import data.constants.SAVE_LL_MODE_ON
import data.constants.TEXT_SIZE
import kotlinx.android.synthetic.main.activity_settings.*
import utils.BaseActivity

class SettingsActivity : BaseActivity() {

    private val textSizeMax = 50
    private val textSizeDefault = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
    }

    fun initViews() {

        val adapter = ArrayAdapter.createFromResource(
                this@SettingsActivity,
                R.array.spinner_names,
                android.R.layout.simple_spinner_item
        )

        spinner_settings.adapter = adapter
        spinner_settings.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) =
                    LAYOUT_PREFERENCE.save(LAYOUT_LIST)

            /**
             * @param p2 is the item which is selected.
             * p2 -> LAYOUT_* in data.constants.DefaultValue
             */
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) =
                    LAYOUT_PREFERENCE.save(p2)
        }

        setTextSizeShowerText(TEXT_SIZE.readInt(textSizeDefault))

        seeker_settings.max = textSizeMax
        seeker_settings.progress = TEXT_SIZE.readInt(textSizeDefault)
        seeker_settings.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            private var size = 16
            override fun onStopTrackingTouch(p0: SeekBar?) = TEXT_SIZE.save(size)
            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 > 5) {
                    setTextSizeShowerText(size)
                    size = p1
                }
            }
        })

        switch_settings.isChecked = SAVE_LL_MODE_ON.readBoolean(false)
        switch_settings.setOnCheckedChangeListener { button, isChecked ->
            SAVE_LL_MODE_ON.save(isChecked)
        }
    }

    private fun setTextSizeShowerText(textSize: Int) {
        text_size_settings_shower.text = "$textSize sp"
        text_size_settings_shower.textSize = textSize.toFloat()
    }

}
