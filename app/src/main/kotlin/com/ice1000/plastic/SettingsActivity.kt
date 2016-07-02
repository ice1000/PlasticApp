package com.ice1000.plastic

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import utils.BaseActivity
import utils.indexLink

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        insert_test_data.setOnClickListener {
            insertIntoSp(
                    indexLink,
                    "====\n周明凯同学自杀\n疑似表白失败"
            )
        }

        delete_test_data.setOnClickListener {
            insertIntoSp(
                    indexLink,
                    DEFAULT_VALUE
            )
        }
    }
}
