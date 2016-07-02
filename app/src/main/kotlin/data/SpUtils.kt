package data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.net.URL

/**
 * @author ice1000
 * Created by asus1 on 2016/7/1.
 */

private val DEFAULT_VALUE = "DEFAULT_VALUE"

fun getStringWebResource(context: Context, res: String, force: Boolean = false): String {
    val preference = openPreference(context)
    var ret = preference.getString(res, DEFAULT_VALUE)
    Log.i("", ret)
    if(ret.equals(DEFAULT_VALUE) || force) {
        ret = URL(res).readText(Charsets.UTF_8)
        val editor = preference.edit()
        editor.putString(res, ret)
        editor.apply()
        return ret
    } else {
        return ret
    }
}

fun insertIntoSp(context: Context, key: String, value: Any) {
    val editor = openPreference(context).edit()
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

fun getBooleanFromSp(context: Context, key: String, default: Boolean = false): Boolean {
    val preference = openPreference(context)
    return preference.getBoolean(key, default)
}

fun getStringFromSp(context: Context, key: String, default: String = ""): String {
    val preference = openPreference(context)
    return preference.getString(key, default)
}

private fun openPreference(context: Context): SharedPreferences =
        context.getSharedPreferences(
                "MainPreference",
                Context.MODE_PRIVATE
        )
