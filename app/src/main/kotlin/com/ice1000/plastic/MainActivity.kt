package com.ice1000.plastic

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import data.BaseData
import data.constants.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import utils.BaseActivity
import java.util.*

class MainActivity : BaseActivity() {

    var index: ArrayList<BaseData> = ArrayList()
    val dataSetOnScreen: RecyclerView
        get() = dataSet_main

    var currentLink = learnLink
    var currentNum = learnNum
    var currentType = TYPE_OTHER_LIST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        refresh(
                link = currentLink,
                dataSize = currentNum,
                dataType = currentType,
                done = { }
        )
    }

    override fun onResume() {
        super.onResume()
        dataSetOnScreen.layoutManager = chooseLayout()
    }

    private fun refresh(
            link: String,
            dataSize: Int,
            dataType: Int,
            done: () -> Unit,
            clean: Boolean = true) {

        Log.i("important", "refreshing, link is $link, have connection = ${
        connection != null}, dataSize = $dataSize")

        val showUselessData = showData(
                indexText = JJ_FLY.split("\n"),
                clean = clean,
                dataSize = dataSize,
                dataType = dataType
        )

        currentLink = link
        currentNum = dataSize
        currentType = dataType

        Log.i("important",
                "currentLink = $currentLink, " +
                        "currentNum = $currentNum, " +
                        "currentType = 0xFF${currentType - 0xFF0}")

        try {
            checkNetwork()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                    this@MainActivity,
                    R.string.please_check_network,
                    Toast.LENGTH_SHORT).show()
            showUselessData
        }

        async() {
            val indexText: List<String>

            indexText = getStringWebResource(link).split("\n")

            uiThread {
                showData(
                        indexText = indexText,
                        clean = clean,
                        dataSize = dataSize,
                        dataType = dataType
                )
                done()
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
//        Log.i("important", "indexText = $indexText")
        if (clean)
            index = ArrayList<BaseData>()
        var i = 0
        while (i < indexText.size) {
            if (indexText[i].startsWith("====")) {
                try {
                    i++
                    if (dataSize == NUMBER_THREE) {
                        index.add(BaseData(
                                title = indexText[i],
                                url = indexText[i + 1],
                                type = dataType,
                                description = indexText[i + 2]
                        ))
                        i += 3
                        continue
                    }
                    if (dataSize == NUMBER_TWO) {
                        index.add(BaseData(
                                title = indexText[i],
                                url = indexText[i + 1],
                                type = dataType,
                                description = FUCKER
                        ))
                        i += 2
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }
            i++
        }
        dataSetOnScreen.adapter = MyAdapter()
//                refresher?.isRefreshing = false

    }

    override fun onBackPressed() {
        refresh(
                link = currentLink,
                done = { },
                dataSize = currentNum,
                dataType = currentType
        )
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
            R.id.action_contributing ->
                startActivity(Intent(
                        this@MainActivity,
                        AboutActivity::class.java
                ))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        setSupportActionBar(toolbar_main)

        dataSetOnScreen.layoutManager = chooseLayout()
        dataSetOnScreen.itemAnimator = DefaultItemAnimator()

        val refresher = refresher_main
        refresher.setOnRefreshListener {
            refresh(
                    link = currentLink,
                    dataSize = currentNum,
                    dataType = currentType,
                    done = {
                        refresher.isRefreshing = false
                    }
            )
        }

        toolbar_learn_main.setOnClickListener {
            refresh(
                    link = learnLink,
                    dataSize = learnNum,
                    dataType = TYPE_OTHER_LIST,
                    done = { }
            )
        }

        toolbar_blog_main.setOnClickListener {
            refresh(
                    link = blogLink,
                    dataSize = blogNum,
                    dataType = TYPE_LIST,
                    done = { }
            )
        }

        toolbar_news_main.setOnClickListener {
            refresh(
                    link = indexLink,
                    dataSize = indexNum,
                    dataType = TYPE_LIST,
                    done = { }
            )
        }
    }

    private fun chooseLayout() = when (
    getIntFromSharedPreference(LAYOUT_PREFERENCE)) {
        LAYOUT_GRID_2 -> GridLayoutManager(this, 2)
        LAYOUT_GRID_3 -> GridLayoutManager(this, 3)
        else -> LinearLayoutManager(this)
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
                viewType: Int) = MyViewHolder(
                LayoutInflater.from(this@MainActivity).inflate(
                        R.layout.data_base,
                        null
                ))

        override fun getItemCount() = index.size

    }

    inner class MyViewHolder(var view: View) :
            RecyclerView.ViewHolder(view) {

        private var lastClick = 0xFF
        private var view1: TextView
        private var view2: TextView

        init {
            view1 = view.find<TextView>(R.id.title_data)
            view2 = view.find<TextView>(R.id.des_data)
        }

        fun init(viewData: BaseData) {
            view1.text = viewData.title
            view2.text = viewData.description

            view.setOnClickListener {
                Log.i("important", "An item is clicked, dataType = 0xFF${
                viewData.type - 0xFF0}")

                when (viewData.type) {

                // 显示一个表，元素点击之后打开网页
                    data.constants.TYPE_LIST ->
                        if (!"null".equals(viewData.url))
                            openWeb(viewData.url)

                // 显示一个表，元素打开之后是另一个表
                    data.constants.TYPE_OTHER_LIST ->
                        if (!"null".equals(viewData.url))
                            refresh(
                                    link = viewData.url,
                                    dataSize = data.constants.NUMBER_THREE,
                                    dataType = data.constants.TYPE_FLOW,
                                    done = { }
                            )

                // 显示一个数据流
                    data.constants.TYPE_FLOW ->
                        startActivity(Intent(
                                this@MainActivity,
                                ScrollingActivity::class.java
                        ).putExtra(URL, viewData.url))
                }
            }

            view.setOnTouchListener { view, event ->
                Log.i("clicked", "event = ${event.action}")
                if (lastClick == 0 && event.action == 1)
                    view.callOnClick()
                view.background = resources.getDrawable(
                        when (event.action) {
                            MotionEvent.ACTION_MOVE,
                            MotionEvent.ACTION_DOWN -> R.drawable.btn_default_light
                            else -> R.drawable.btn_default_more_light
                        })
                lastClick = event.action
                return@setOnTouchListener true
            }
        }
    }

}
