package com.ice1000.plastic

import android.os.Bundle
import data.JJFLY

import kotlinx.android.synthetic.main.activity_settings.*
import utils.BaseActivity
import utils.indexLink
class SettingsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        insert_test_data.setOnClickListener {
            insertIntoSp(indexLink, JJFLY)
        }

        delete_test_data.setOnClickListener {
            insertIntoSp(
                    indexLink,
                    DEFAULT_VALUE
            )
        }
    }
}
