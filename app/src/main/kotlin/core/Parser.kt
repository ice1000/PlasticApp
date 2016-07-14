package core

import android.util.Log
import data.BaseData
import data.constants.FUCKER
import data.modules.Module
import java.util.*

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/12.
 */
object Parser {

    /**
     * @param source DSL source
     */
    fun parse(source: List<String>, dataType: Int, dataSize: Int): ArrayList<BaseData> {
        val index = ArrayList<BaseData>()
        var i = 0
        while (i < source.size) {
            if (source[i].startsWith("def ")) {
                val definition = source[i].split(" ").toList().filter {
                    it.length > 0
                }
//                测试正确，definition获取了正确的信息。
                println("%" + definition[1] + "%")
                source.forEach {
                    it.replace(
                            oldValue = "%" + definition[1] + "%",
                            newValue = definition[2]
                    )
                    println(it)
                }
            }
            if (source[i].startsWith("====")) {
                try {
                    i++
                    if (dataSize == Module.NUMBER_THREE) {
                        index.add(BaseData(
                                title = source[i],
                                url = source[i + 1],
                                type = dataType,
                                description = source[i + 2]
                        ))
                        i += 3
                        continue
                    }
                    if (dataSize == Module.NUMBER_TWO) {
                        index.add(BaseData(
                                title = source[i],
                                url = source[i + 1],
                                type = dataType,
                                description = FUCKER
                        ))
                        i += 2
                    }
                } catch (e: IndexOutOfBoundsException) {
                    Log.i("important", "parsing indexOutOfBound!!!")
                }
                Log.i("important", "parse finished")
            }
            // !!!VERY IMPORTANT!!!
            i++
        }
        return index
    }
}
