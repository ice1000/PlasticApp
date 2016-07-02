package com.ice1000.plastic

import android.os.Bundle
import android.view.View
import data.insertIntoSp
import utils.BaseActivity
import utils.indexLink

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun insertData(view: View) {
        insertIntoSp(
                this@SettingsActivity,
                indexLink,
                "====\n周明凯同学自杀\n疑似表白失败"
        )
    }
}
