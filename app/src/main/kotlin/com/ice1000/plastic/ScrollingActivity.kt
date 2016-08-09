package com.ice1000.plastic

import android.os.Bundle
import android.util.Log
import data.constants.TEXT_SIZE
import kotlinx.android.synthetic.main.activity_scrolling.*
import utils.BaseActivity

class ScrollingActivity : BaseActivity() {

    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        url = intent.getStringExtra(URL)
        initViews()
        refresh({ })
        Log.d(this.toString(), url)
    }

    private fun initViews() {
        fab_scrolling.setOnClickListener({ openWeb(url) })
        pull_scrolling.setOnRefreshListener { refresh({ pull_scrolling.isRefreshing = false }) }
        data_scrolling.textSize = TEXT_SIZE.readInt(16).toFloat()
    }

    private fun refresh(end: () -> Unit) {
        url.webResource({ s ->
            end()
            data_scrolling.text = s
        })
    }
}
