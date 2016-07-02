package utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ice1000.plastic.WebViewerActivity

/**
 * @author ice1000
 * Created by asus1 on 2016/6/4.
 */
open class BaseActivity : AppCompatActivity() {

    protected val URL = "URL"

    protected fun openWeb(url: String) {
        var intent = Intent(this, WebViewerActivity::class.java)
        intent.putExtra(URL, url)
        startActivity(intent)
    }

    private val DEFAULT_VALUE = "DEFAULT_VALUE"

    protected fun getStringWebResource(res: String,
            haveConnection: Boolean = false): String {
        var ret = getStringFromSp(res, DEFAULT_VALUE)
        Log.i("important", ret)
        if(ret.equals(DEFAULT_VALUE) || haveConnection) {
            Log.i("important", "linking to web")
            ret = java.net.URL(res).readText(Charsets.UTF_8)
            insertIntoSp(res, ret)
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
