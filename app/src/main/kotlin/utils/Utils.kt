package utils

import android.content.Context
import android.util.Log
import java.net.URL

/**
 * @author ice1000
 * Created by asus1 on 2016/7/1.
 */

private val DEFAULT_VALUE = "t3ujhbfsdcknfr3o29758rhyjuvfweico;03p9ut6ui"

fun getStringWebResource(res: String, context: Context, force: Boolean = false): String {
    val preference = context.getSharedPreferences(
            "MainPreference",
            Context.MODE_PRIVATE
    )
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
