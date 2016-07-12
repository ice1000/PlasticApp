package com.ice1000.plastic

import android.os.Bundle
import android.util.Log
import android.widget.`@+id/save_ll_mode_title`
import data.constants.TEXT_SIZE
import kotlinx.android.synthetic.main.activity_scrolling.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import utils.BaseActivity

class ScrollingActivity : BaseActivity() {

    val data: `@+id/save_ll_mode_title`
        get() = data_scrolling
    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        url = intent.getStringExtra(URL)
        Log.d(this.toString(), url)
        initViews()
        refresh({})
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
        data.textSize = TEXT_SIZE.readInt(16).toFloat()
    }

    private fun refresh(end: () -> Unit) {
        async() {
            val text = url.webResource()
            uiThread {
                end()
                data.text = text
            }
        }
    }
}
