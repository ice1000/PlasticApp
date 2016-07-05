package utils

import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ice1000.plastic.WebViewerActivity

/**
 * @author ice1000
 * Created by asus1 on 2016/6/4.
 */
open class BaseActivity : AppCompatActivity() {

    protected val URL = "URL"
    var connection: ConnectivityManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connection = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    protected fun openWeb(url: String) {
        startActivity(Intent(
                this,
                WebViewerActivity::class.java
        ).putExtra(URL, url))
    }

    protected fun checkNetwork(): Boolean {
        Log.v("not important", "connection?.activeNetworkInfo = " +
                "${connection!!.activeNetworkInfo}")
        return connection!!.activeNetworkInfo != null
    }

    protected val DEFAULT_VALUE = "DEFAULT_VALUE"

    /**
     * this will cache the data into SharedPreference
     * next time when the network is invalid, it will return the data
     * stored in the SharedPreference.
     * @param url url
     */
    protected fun getStringWebResource(
            url: String): String {
        var ret = getStringFromSp(url, DEFAULT_VALUE)
        Log.i("important", "ret = $ret")
        if(ret.equals(DEFAULT_VALUE)
                || checkNetwork()) {
            Log.i("important", "linking to web")
            ret = java.net.URL(url).readText(Charsets.UTF_8)
            insertIntoSp(url, ret)
            return ret
        } else {
            Log.i("important", "linking to SharedPreference")
            return ret
        }
    }

    protected fun insertIntoSp(key: String, value: Any) {
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
            Log.i("important", "value = $value")
            editor.putString(key, value)
        } else {
            throw Exception("not supported type")
        }
        editor.apply()
    }

    protected fun getBooleanFromSp(key: String, default: Boolean = false): Boolean {
        val preference = openPreference()
        return preference.getBoolean(key, default)
    }

    protected fun getStringFromSp(key: String, default: String = ""): String {
        val preference = openPreference()
        return preference.getString(key, default)
    }

    private fun openPreference(): SharedPreferences =
            getSharedPreferences(
                    "MainPreference",
                    MODE_APPEND
            )

}
