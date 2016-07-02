package utils

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
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
}
