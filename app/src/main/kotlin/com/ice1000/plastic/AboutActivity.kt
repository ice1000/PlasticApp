package com.ice1000.plastic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import data.constants.QQ_GROUP_PL_VALUE
import data.constants.TEXT_SIZE
import data.modules.BlogAndOther
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import utils.BaseActivity

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val fab = fab_setting
        fab.setOnClickListener({
            joinQQGroup(BlogAndOther.qqLink.webResource(
                    QQ_GROUP_PL_VALUE
            ))
        })

        viewGiuHub.setOnClickListener {
            viewGitHub()
        }

        async() {
            val fuck = BlogAndOther.appreciateLink.webResource()
            uiThread {
                contributeText.text = fuck
                contributeText.textSize = TEXT_SIZE.readInt(16).toFloat()
            }
        }
    }

    fun viewGitHub() = openWeb("https://github.com/ice1000/PlasticApp")

    /****************
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com" +
                "%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key)
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，
        // 返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent)
            return true
        } catch (e: Exception) {
            toast(getString(R.string.please_install_qq))
            return false
        }
    }
}
