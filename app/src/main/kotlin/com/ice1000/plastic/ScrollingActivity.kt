package com.ice1000.plastic

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_scrolling.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import utils.BaseActivity

class ScrollingActivity : BaseActivity() {

    var data: TextView? = null
    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        url = intent.getStringExtra(URL)
        Log.d(this.toString(), url)
        initViews()
        refresh({
        })
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
        data = data_scrolling
    }

    private fun refresh(end: () -> Unit) {
        async() {
            val text = getStringWebResource(url)
            uiThread {
                end()
                data?.text = text
            }
        }
    }
}
