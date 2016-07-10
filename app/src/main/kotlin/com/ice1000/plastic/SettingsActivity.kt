package com.ice1000.plastic

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import data.constants.LAYOUT_LIST
import data.constants.LAYOUT_PREFERENCE
import data.constants.TEXT_SIZE
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.toast
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

        val layoutSpinner = spinner_settings_layout
        layoutSpinner.adapter = adapter
        layoutSpinner.onItemSelectedListener = SpinnerSelected()

        val textSizeEditor = text_size_settings
        textSizeEditor.text.append(TEXT_SIZE.readInt(16).toString())
        textSizeEditor.onFocusChangeListener =
                View.OnFocusChangeListener { view, onAction ->
                    try {
                        val a = Integer.parseInt(textSizeEditor.text.toString().trim())
                        if(!onAction) {
                            if(a < 100 && a > 5) {
                                TEXT_SIZE.save(a)
                            } else {
                                throw Exception("对方不想和你说话，并向你抛出了一个异常")
                            }
                        }
                    } catch (e: Exception) {
                        toast(resources.getString(
                                R.string.please_input_valid_num))
                    }
                }
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
}
