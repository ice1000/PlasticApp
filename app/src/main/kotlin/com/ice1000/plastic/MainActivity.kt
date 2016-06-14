package com.ice1000.plastic

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import utils.memberLink
import java.net.URL
import java.util.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var index: ArrayList<BaseData> = ArrayList()
//    var index: List<BaseData>? = null
    var dataSetOnScreen: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        refresh()
    }

    private fun refresh(link: String = indexLink, dataSize:Int = 3) {
        async() {
            var indexText = URL(link).readText(Charsets.UTF_8).split("\n")
            uiThread {
                index = ArrayList<BaseData>()
                var i = 0;
                while (i < indexText.size) {
                  if (indexText[i].startsWith("====") {
                    i++
                    if (dataSize == 3) {
                        index.add(BaseData(
                                indexText[i],
                                indexText[i + 1],
                                indexText[i + 2]
                        ))
                        i += 3
                        continue
                    }
                    if (dataSize == 2) {
                        index.add(BaseData(
                                indexText[i],
                                indexText[i + 1],
                                "                                                                      "
                        ))
                        i += 2
                    }
                  }
                  i++
              }
                dataSetOnScreen?.adapter = MyAdapter()
            }
        }
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
            R.id.action_refresh -> refresh()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_news -> refresh(indexLink, 3)
            R.id.nav_members -> refresh(memberLink, 2)
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

        dataSetOnScreen = dataSet
        dataSetOnScreen?.layoutManager = LinearLayoutManager(this)
        dataSetOnScreen?.itemAnimator = DefaultItemAnimator()

        val drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)

    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            holder?.init(index[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
                = MyViewHolder(LayoutInflater.from(this@MainActivity).inflate(
                        R.layout.data_base,
                        null
        ))

        override fun getItemCount(): Int {
            Log.v("", "index.size = ${index.size}")
            return index.size
        }

    }
    inner class MyViewHolder : RecyclerView.ViewHolder {

        private var view1: TextView? = null
        private var view2: TextView? = null

        constructor(view: View) : super(view) {
            view1 = view.findViewById(R.id.messageBox).findViewById(R.id.title) as TextView?
            view2 = view.findViewById(R.id.messageBox).findViewById(R.id.des) as TextView?
            Log.v("", "views are " + if(view1 == null) "null" else "OK")
            view.setOnTouchListener { view, event ->
                when(event.action) {
                    0, 2 -> {
                        view.setBackgroundColor(Color.GRAY)
                    }
                    else -> {
                        view.setBackgroundColor(Color.WHITE)
                    }
                }
                true
            }
        }

        fun init(data: BaseData) {
            view1?.text = data.title
            view2?.text = data.description
        }
    }
}
