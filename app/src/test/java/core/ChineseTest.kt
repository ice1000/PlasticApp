package core

import org.junit.Test
import java.util.*

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/21.
 */
class ChineseTest() {

    @Test
    fun 中文测试() {
        var 我是一个中文变量啦啦啦啦 = 1
        while (我是一个中文变量啦啦啦啦 < 5) {
            我是一个中文变量啦啦啦啦++
            println("我是一个中文变量啦啦啦啦 = $我是一个中文变量啦啦啦啦")
            println("中文函数： 随机结果 = ${我是一个中文函数啦啦啦()}")
        }
        val 我是另一个中文变量哈哈哈 = 中文类名测试哈哈哈(this@ChineseTest)
        println("我是另一个中文变量哈哈哈.千里冰封 = ${我是另一个中文变量哈哈哈.千里冰封}")
    }

    override fun toString(): String {
        return "大家好我是中文测试"
    }

    fun 我是一个中文函数啦啦啦() = Random().nextInt()

    inner class 中文类名测试哈哈哈(val 千里冰封: ChineseTest) {
        // some codes
    }

}
