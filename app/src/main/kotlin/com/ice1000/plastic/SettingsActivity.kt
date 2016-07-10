package com.ice1000.plastic

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import data.constants.LAYOUT_LIST
import data.constants.LAYOUT_PREFERENCE
import kotlinx.android.synthetic.main.activity_settings.*
import utils.BaseActivity

class SettingsActivity : BaseActivity() {

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
        val spinner = spinner_settings_layout
        spinner.adapter = adapter
        spinner.onItemSelectedListener = SpinnerSelected()

    }

    inner class SpinnerSelected() : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            insertIntoSharedPreference(
                    key = LAYOUT_PREFERENCE,
                    value = LAYOUT_LIST
            )
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
            insertIntoSharedPreference(
                    key = LAYOUT_PREFERENCE,
                    value = p2
            )
        }
    }
}
