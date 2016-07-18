package utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ice1000.plastic.WebViewerActivity
import data.constants.SAVE_LL_MODE_ON
import org.jetbrains.anko.intentFor

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
        startActivity(intentFor<WebViewerActivity>()
                .putExtra(URL, url))
    }

    protected fun checkNetwork(): Boolean {
        Log.v("not important", "connection? = " +
                "${connection ?: "no network found!"}")
        return connection != null && connection!!.isConnected
    }

    protected val DEFAULT_VALUE = "DEFAULT_VALUE"

    /**
     * this will cache the data into SharedPreference
     * next time when the network is invalid, it will return the data
     * stored in the SharedPreference.
     *
     * use this in asnyc!!!!
     *
     * this method extended String.
     */
    fun String.webResource(default: String = DEFAULT_VALUE): String {
        var ret = readString(default)
//        Log.i("important", "ret = $ret")
        if (SAVE_LL_MODE_ON.readBoolean(false) ||
                !ret.equals(DEFAULT_VALUE) &&
                !checkNetwork()) {
            Log.i("important", "linking to SharedPreference")
            return ret
        } else {
            Log.i("important", "linking to web")
            ret = java.net.URL(this).readText(Charsets.UTF_8)
            save(ret)
            return ret
        }
    }

    /**
     * insert a value t SharedPreference
     * any types of value is accepted.
     *
     * Will be start casted.
     */
    fun String.save(value: Any) {
        val editor = openPreference().edit()
        if (value is Int) {
            editor.putInt(this, value)
        } else if (value is Float) {
            editor.putFloat(this, value)
        } else if (value is Long) {
            editor.putLong(this, value)
        } else if (value is Boolean) {
            editor.putBoolean(this, value)
        } else if (value is String) {
//            Log.i("important", "value = $value")
            editor.putString(this, value)
        } else {
            throw Exception("not supported type")
        }
        editor.apply()
    }

    fun String.readString(default: String = "") =
            openPreference().getString(this, default) ?: ""

    fun String.readInt(default: Int = 0) =
            openPreference().getInt(this, default)

    fun String.readBoolean(default: Boolean = false) =
            openPreference().getBoolean(this, default)


    /**
     * @return a SharedPreference
     */
    private fun openPreference(): SharedPreferences =
            getSharedPreferences(
                    "MainPreference",
                    MODE_ENABLE_WRITE_AHEAD_LOGGING
            )

}
