package com.ice1000.plastic

import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_web_viewer.*
import utils.BaseActivity

class WebViewerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viewer)

//        actionBar.setDisplayShowHomeEnabled(true)

        val url = intent.getStringExtra(URL)

        Log.i(this.toString(), "url = $url")

        webViewer.loadUrl(url)
    }
}
