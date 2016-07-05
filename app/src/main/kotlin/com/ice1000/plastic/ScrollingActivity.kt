package com.ice1000.plastic

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_scrolling.*
import utils.BaseActivity

class ScrollingActivity : BaseActivity() {

    var recycler: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        initViews()
    }

    private fun initViews() {
        val fab = fab_scrolling
        fab.setOnClickListener({
        })
        recycler = recycler_scrolling
    }

//    inner class
}
