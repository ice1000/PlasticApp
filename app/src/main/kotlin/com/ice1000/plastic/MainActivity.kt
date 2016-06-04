package com.ice1000.plastic

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import data.BaseData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import utils.BaseActivity
import utils.indexLink
import java.net.URL
import java.util.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var index: ArrayList<BaseData> = ArrayList()
    var indexText: List<String> = emptyList()
    var dataSetOnScreen: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        async() {
            indexText = URL(indexLink).readText(Charsets.UTF_8).split("\n") as MutableList<String>
            uiThread { refresh() }
        }
    }

    private fun refresh() {
        index.removeAll(index)
        var i = 0;
        while (i < indexText.size) {
            index.add(BaseData(indexText[i], indexText[i + 1]))
//            SpUtils.put(this, "index1", indexText[i])
//            SpUtils.put(this, "index2", indexText[i + 1])
            i += 2
        }
//        for ((title, url, des) in index) {
//            var a = layoutInflater.inflate(R.layout.data_base, null)
//            a.find<TextView>(R.id.title).text = title
//            a.find<TextView>(R.id.des).text = url
//            a.setOnClickListener {
//                openWeb(url)
//            }
//        }
    }

    override fun onBackPressed() {
        val drawer = drawer_layout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_news -> {
                //
            }
            R.id.nav_contribute ->
                startActivity(Intent(this, ScrollingActivity::class.java))
        }
        val drawer = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initViews() {
        val toolbar = toolbar
        setSupportActionBar(toolbar)

        val drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)

        dataSetOnScreen = dataSet
        dataSetOnScreen?.adapter = MyAdapter()
        dataSetOnScreen?.layoutManager = LinearLayoutManager(this)
        dataSetOnScreen?.itemAnimator = DefaultItemAnimator()
    }

    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            holder?.init(index[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = MyViewHolder(
                LayoutInflater.from(this@MainActivity).inflate(
                        R.layout.data_base,
                        parent,
                        false
                )
        )

        override fun getItemCount(): Int = index.size

        inner class MyViewHolder : RecyclerView.ViewHolder {

            private var view1: TextView? = null
            private var view2: TextView? = null

            constructor(view: View) : super(view) {
                view1 = findViewById(R.id.title) as TextView?
                view2 = findViewById(R.id.des) as TextView?
            }

            fun init(data: BaseData) {
                view1?.text = data.title
                view2?.text = data.url
            }
        }
    }
}
