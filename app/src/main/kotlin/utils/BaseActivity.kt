package utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ice1000.plastic.R
import com.ice1000.plastic.WebViewerActivity
import org.jetbrains.anko.toast

/**
 * @author ice1000
 * Created by ice1000 on 2016/6/4.
 */
open class BaseActivity : AppCompatActivity() {

    protected val URL = "URL"

    /**
     * if this is null,
     * it means I have 2 load data from Sp.
     */
    val connection: NetworkInfo?
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager).activeNetworkInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun openWeb(url: String) {
        startActivity(Intent(
                this@BaseActivity,
                WebViewerActivity::class.java
        ).putExtra(URL, url))
    }

    protected fun checkNetwork(): Boolean {
        Log.v("not important", "connection? = " +
                "${connection ?: "no network found!"}")
        toast(R.string.please_check_network)
        return connection != null
    }

    protected val DEFAULT_VALUE = "DEFAULT_VALUE"

    /**
     * this will cache the data into SharedPreference
     * next time when the network is invalid, it will return the data
     * stored in the SharedPreference.
     *
     * use this in asnyc!!!!
     *
     * @param url url
     */
    protected fun getStringWebResource(
            url: String): String {
        var ret = getStringFromSharedPreference(url, DEFAULT_VALUE)
//        Log.i("important", "ret = $ret")
        if(ret.equals(DEFAULT_VALUE)
                || checkNetwork()) {
            Log.i("important", "linking to web")
            ret = java.net.URL(url).readText(Charsets.UTF_8)
            insertIntoSharedPreference(url, ret)
            return ret
        } else {
            Log.i("important", "linking to SharedPreference")
            return ret
        }
    }

    /**
     * insert a value t SharedPreference
     * any types of value is accepted.
     *
     * Will be start casted.
     */
    protected fun insertIntoSharedPreference(key: String, value: Any) {
        val editor = openPreference().edit()
        if(value is Int) {
            editor.putInt(key, value)
        } else if(value is Float) {
            editor.putFloat(key, value)
        } else if(value is Long) {
            editor.putLong(key, value)
        } else if(value is Boolean) {
            editor.putBoolean(key, value)
        } else if(value is String) {
//            Log.i("important", "value = $value")
            editor.putString(key, value)
        } else {
            throw Exception("not supported type")
        }
        editor.apply()
    }

    protected fun getStringFromSharedPreference(
            key: String,
            default: String = ""): String {
        return openPreference().getString(key, default)
    }

    protected fun getIntFromSharedPreference(
            key: String,
            default: Int = 0): Int {
        return openPreference().getInt(key, default)
    }

    /**
     * @return a SharedPreference
     */
    private fun openPreference(): SharedPreferences =
            getSharedPreferences(
                    "MainPreference",
                    MODE_ENABLE_WRITE_AHEAD_LOGGING
            )

}
