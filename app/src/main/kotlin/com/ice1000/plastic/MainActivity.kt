package com.ice1000.plastic

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.SwipeRefreshLayout
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
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import utils.*
import java.util.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var index: ArrayList<BaseData> = ArrayList()
    //    var index: List<BaseData>? = null
    var dataSetOnScreen: RecyclerView? = null
    var connection: ConnectivityManager? = null
    var refresher: SwipeRefreshLayout? = null

    val NUMBER_TWO = 0x2
    val NUMBER_THREE = 0x3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connection = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        initViews()
        refresh()
    }

    private fun refresh(
            link: String = learnLink,
            dataSize: Int = NUMBER_THREE,
            clean: Boolean = true) {

        async() {
            val indexText: List<String>
            if (connection?.activeNetworkInfo == null) {
                toast(getString(R.string.please_check_network))
            }
            indexText = getStringWebResource(
                    link,
                    this@MainActivity,
                    // 这里我觉得我写得很厉害，如果没有网络连接那就会返回null，
                    // 然后这里就是false，就会读取内部信息。
                    connection?.activeNetworkInfo != null
            ).split("\n")
            uiThread {
                if (clean)
                    index = ArrayList<BaseData>()
                var i = 0
                while (i < indexText.size) {
                    if (indexText[i].startsWith("====")) {
                        i++
                        if (dataSize == NUMBER_THREE) {
                            index.add(BaseData(
                                    indexText[i],
                                    indexText[i + 1],
                                    indexText[i + 2]
                            ))
                            i += 3
                            continue
                        }
                        if (dataSize == NUMBER_TWO) {
                            index.add(BaseData(
                                    indexText[i],
                                    indexText[i + 1],
                                    ""
                            ))
                            i += 2
                        }
                    }
                    i++
                }
                dataSetOnScreen?.adapter = MyAdapter()
                refresher?.isRefreshing = false
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
//            R.id.action_refresh -> refresh()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_news ->
                refresh(indexLink, NUMBER_THREE)
            R.id.nav_members ->
                refresh(memberLink, NUMBER_TWO)
            R.id.nav_learn ->
                refresh(learnLink, NUMBER_TWO)
            R.id.nav_blogs ->
                refresh(learnLink, NUMBER_THREE)
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

        refresher = find(R.id.refresher)
        refresher?.setOnRefreshListener {
            refresh()
        }
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
            return index.size
        }

    }

    inner class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        private var view1 = view.findViewById(R.id.title) as TextView?
        private var view2 = view.findViewById(R.id.des) as TextView?

        fun init(data: BaseData) {
            view1?.text = data.title
            view2?.text = data.description
            view.setOnClickListener {
                if (!data.url.equals("null"))
                    openWeb(data.url)
            }
        }
    }

}
