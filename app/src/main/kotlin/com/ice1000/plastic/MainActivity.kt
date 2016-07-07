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
import org.jetbrains.anko.uiThread
import utils.BaseActivity
import java.util.*

class MainActivity : BaseActivity() {

    var index: ArrayList<BaseData> = ArrayList()
    var dataSetOnScreen: RecyclerView? = null

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
        dataSetOnScreen?.layoutManager = chooseLayout()
    }

    private fun refresh(
            link: String,
            dataSize: Int,
            dataType: Int,
            done: () -> Unit,
            clean: Boolean = true) {

        Log.i("important", "refreshing, link is $link, have connection = ${
        connection?.activeNetworkInfo != null
        }, dataSize = $dataSize")

        val showUselessData = showData(
                JJ_FLY.split("\n"),
                clean,
                dataSize,
                dataType
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
                        indexText,
                        clean,
                        dataSize,
                        dataType
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
                } catch (e: IndexOutOfBoundsException) {
                }
            }
            i++
        }
        dataSetOnScreen?.adapter = MyAdapter()
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
                        this,
                        AboutActivity::class.java
                ))
        }
        return super.onOptionsItemSelected(item)
    }

    fun toolbarNews(view: View) =
            refresh(
                    link = indexLink,
                    dataSize = indexNum,
                    dataType = TYPE_LIST,
                    done = { }
            )

    fun toolbarLearn(view: View) =
            refresh(
                    link = learnLink,
                    dataSize = learnNum,
                    dataType = TYPE_OTHER_LIST,
                    done = { }
            )

    fun toolbarBlogs(view: View) =
            refresh(
                    link = blogLink,
                    dataSize = blogNum,
                    dataType = TYPE_LIST,
                    done = { }
            )

    private fun initViews() {
        setSupportActionBar(toolbar)

        dataSetOnScreen = dataSet_main
        dataSetOnScreen?.layoutManager = chooseLayout()
        dataSetOnScreen?.itemAnimator = DefaultItemAnimator()

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
//            refresher.isRefreshing = false
        }
    }

    private fun chooseLayout() = when(
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
                viewType: Int)
                = MyViewHolder(LayoutInflater.from(this@MainActivity).inflate(
                R.layout.data_base,
                null
        ))

        override fun getItemCount() = index.size

    }

    inner class MyViewHolder(var view: View) :
            RecyclerView.ViewHolder(view) {

        private var view1 = view.findViewById(R.id.title) as TextView?
        private var view2 = view.findViewById(R.id.des) as TextView?
        private var lastClick = 0xFF

        fun init(viewData: BaseData) {
            view1?.text = viewData.title
            view2?.text = viewData.description

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
                when (event.action) {
//                    MotionEvent.ACTION_BUTTON_PRESS ->{
//                        view.background = resources.getDrawable(R.drawable.btn_default_light)
//                        view.callOnClick()
//                    }
//                    MotionEvent.ACTION_OUTSIDE
                    MotionEvent.ACTION_MOVE,
                    MotionEvent.ACTION_DOWN -> view.background =
                            resources.getDrawable(R.drawable.btn_default_light)
                    else -> view.background =
                            resources.getDrawable(R.drawable.btn_default_more_light)
                }
                lastClick = event.action
                return@setOnTouchListener true
            }
        }
    }

}
