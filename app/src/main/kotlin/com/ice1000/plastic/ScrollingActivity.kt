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
        refresh({})
        Log.d(this.toString(), url)
    }

    private fun initViews() {
        val fab = fab_scrolling
        fab.setOnClickListener({
            openWeb(url)
        })
        val refresher = refresher_scrolling
        refresher.setOnRefreshListener {
            refresh({
                refresher.isRefreshing = false
            })
        }
        data_scrolling.textSize = TEXT_SIZE.readInt(16).toFloat()
    }

    private fun refresh(end: () -> Unit) {
        url.webResource({ s ->
            end()
            data_scrolling.text = s
        })
    }
}
