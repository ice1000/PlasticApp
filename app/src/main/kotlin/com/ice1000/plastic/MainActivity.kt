package com.ice1000.plastic

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import data.BaseData
import data.JJFLY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import utils.*
import java.util.*

class MainActivity : BaseActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    var index: ArrayList<BaseData> = ArrayList()
    var dataSetOnScreen: RecyclerView? = null
    var refresher: SwipeRefreshLayout? = null

    var currentLink = learnLink
    var currentNum = learnNum
    var currentType = listType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        refresh()
    }

    private fun refresh(
            link: String = currentLink,
            dataType: Int = currentType,
            dataSize: Int = currentNum,
            clean: Boolean = true) {

        Log.i("important", "refreshing, link is $link, have connection = ${
            connection?.activeNetworkInfo != null
        }, dataSize = $dataSize")

        currentLink = link
        currentNum = dataSize
        currentType = dataType

        try {
            Log.v("not important", "connection?.activeNetworkInfo = " +
                    "${connection!!.activeNetworkInfo}")
        } catch (e: Exception) {
            toast(getString(R.string.please_check_network))
            showData(
                    JJFLY.split("\n"),
                    clean,
                    dataSize,
                    dataType
            )
            return
        }
        async() {
            val indexText: List<String>

            indexText = getStringWebResource(link,
                    connection!!.activeNetworkInfo != null
            ).split("\n")

            uiThread {
                showData(
                        indexText,
                        clean,
                        dataSize,
                        dataType
                )
            }
        }
    }

    /**
     * core code of this system
     */
    private fun showData(
            indexText: List<String>,
            clean: Boolean,
            dataSize: Int,
            dataType: Int) {
        Log.i("important", "indexText = $indexText")
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
                            dataType,
                            indexText[i + 2]
                    ))
                    i += 3
                    continue
                }
                if (dataSize == NUMBER_TWO) {
                    index.add(BaseData(
                            indexText[i],
                            indexText[i + 1],
                            dataType,
                            ""
                    ))
                    i += 2
                }
            }
            i++
        }
        dataSetOnScreen?.adapter = MyAdapter()
//                refresher?.isRefreshing = false

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
                startActivity(Intent(
                        this@MainActivity,
                        SettingsActivity::class.java
                ))
                return true
            }
//            R.id.action_refresh -> refresh()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_news ->
                refresh(indexLink, indexNum, listType)
//            R.id.nav_members ->
//                refresh(memberLink, memberNum, listType)
            R.id.nav_learn ->
                refresh(learnLink, learnNum, otherListType)
            R.id.nav_blogs ->
                refresh(blogLink, blogNum, listType)
            R.id.nav_contribute ->
                startActivity(Intent(
                        this, 
                        ScrollingActivity::class.java
                ))
        }
        val drawer = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initViews() {
        val toolbar = toolbar
        setSupportActionBar(toolbar)

        dataSetOnScreen = dataSet_main
        dataSetOnScreen?.layoutManager = LinearLayoutManager(this)
        dataSetOnScreen?.itemAnimator = DefaultItemAnimator()

        val drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)

        refresher = refresher_main
        refresher?.setOnRefreshListener {
            refresh()
            refresher!!.isRefreshing = false
        }
    }

    inner class MyAdapter :
            RecyclerView.Adapter<MyViewHolder>() {
        override fun onBindViewHolder(
                holder: MyViewHolder?,
                position: Int) {
            holder?.init(index[position])
        }

        override fun onCreateViewHolder(
                parent: ViewGroup?,
                viewType: Int)
                = MyViewHolder(LayoutInflater.from(this@MainActivity).inflate(
                R.layout.data_base,
                null
        ))

        override fun getItemCount(): Int {
            return index.size
        }

    }

    inner class MyViewHolder(var view: View) :
            RecyclerView.ViewHolder(view) {

        private var view1 = view.findViewById(R.id.title) as TextView?
        private var view2 = view.findViewById(R.id.des) as TextView?

        fun init(data: BaseData) {
            view1?.text = data.title
            view2?.text = data.description

            view.setOnClickListener {
                when(data.type) {
                    // 显示一个表，元素点击之后打开网页
                    listType ->
                        if (!"null".equals(data.url))
                            openWeb(data.url)
                    // 显示一个表，元素打开之后是另一个表
                    otherListType ->
                        if (!"null".equals(data.url))
                            refresh(
                                    data.url,
                                    flowType,
                                    NUMBER_THREE
                            )
                    // 显示一个数据流
                    flowType ->
                        startActivity(Intent(
                                this@MainActivity,
                                ScrollingActivity::class.java
                        ).putExtra(URL, data.url))
                }
            }

            view.setOnTouchListener { view, event ->
                when(event.action) {
                    MotionEvent.ACTION_BUTTON_PRESS -> view.background =
                            resources.getDrawable(R.drawable.btn_default_light)
                    MotionEvent.ACTION_MOVE -> view.background =
                            resources.getDrawable(R.drawable.btn_default_light)
                    else -> view.background =
                            resources.getDrawable(R.drawable.btn_default_more_light)
                }
                return@setOnTouchListener true
            }
        }
    }

}
