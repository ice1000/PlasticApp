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

        spinner_settings_layout.adapter = adapter
        spinner_settings_layout.onItemSelectedListener =
                SpinnerSelected()

        setTextSizeShowerText(TEXT_SIZE.readInt(textSizeDefault))

        text_size_settings_seeker.max = textSizeMax
        text_size_settings_seeker.progress = TEXT_SIZE.readInt(textSizeDefault)
        text_size_settings_seeker.setOnSeekBarChangeListener(
                text_size_settings_seeker({
                    setTextSizeShowerText(it)
                }))

        save_ll_switch_settings.isChecked = SAVE_LL_MODE_ON.readBoolean(false)
        save_ll_switch_settings.setOnCheckedChangeListener { button, isChecked ->
            SAVE_LL_MODE_ON.save(isChecked)
        }
    }

    private fun setTextSizeShowerText(textSize: Int) {
        text_size_settings_shower.text = "$textSize sp"
        text_size_settings_shower.textSize = textSize.toFloat()
    }

    inner class SpinnerSelected() : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(p0: AdapterView<*>?) {
            LAYOUT_PREFERENCE.save(LAYOUT_LIST)
        }

        /**
         * @param p2 is the item which is selected.
         * p2 -> LAYOUT_* in data.constants.DefaultValue
         */
        override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long) {
            LAYOUT_PREFERENCE.save(p2)
        }
    }

    inner class text_size_settings_seeker(var shower: (Int) -> Unit) :
            SeekBar.OnSeekBarChangeListener {

        private var size = 16

        /**
         * textSize must > 6
         */
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            if (p1 > 5) {
                shower(size)
                size = p1
            }
//            Log.v("important", "progress = $size")
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
//            Log.v("important", "start, max = $max")
        }

        override fun onStopTrackingTouch(p0: SeekBar?) =
            TEXT_SIZE.save(size)

    }
}
