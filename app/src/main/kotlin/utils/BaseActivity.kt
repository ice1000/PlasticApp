package utils

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity

/**
 * @author ice1000
 * Created by asus1 on 2016/6/4.
 */
open class BaseActivity : AppCompatActivity() {
    protected fun openWeb(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}
