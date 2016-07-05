package com.ice1000.plastic

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_scrolling.*
import utils.BaseActivity

class ScrollingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        val fab = fab
        fab.setOnClickListener({
        })

        viewGiuHub.setOnClickListener {
        }
    }
}
