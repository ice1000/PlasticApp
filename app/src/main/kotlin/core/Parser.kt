package core

import android.util.Log
import data.BaseData
import data.constants.FUCKER
import data.modules.Module
import java.util.*

/**
 * @author ice1000
 * @version 0.3.2
 * Created by ice1000 on 2016/7/12.
 */
object Parser {

    var source: List<String> = listOf()

    /**
     * @param source DSL source
     */
    fun parse(
            source: List<String>,
            dataType: Int,
            dataSize: Int
    ): ArrayList<BaseData> {
        this.source = source
        val index = ArrayList<BaseData>()
        var i = 0
        while (i < this.source.size) {
            if (this.source[i].startsWith("def ")) {
                val definition = this.source[i].split(" ").toList().filter {
                    it.length > 0
                }
                this.source = source.map {
                    it.replace(
                            oldValue = "%" + definition[1] + "%",
                            newValue = definition[2]
                    )
                }
            }
            if (this.source[i].startsWith("====")) {
                try {
                    i++
                    if (dataSize == Module.NUMBER_THREE) {
                        index.add(BaseData(
                                title = this.source[i],
                                url = this.source[i + 1],
                                type = dataType,
                                description = this.source[i + 2]
                        ))
                        i += 3
                        continue
                    }
                    if (dataSize == Module.NUMBER_TWO) {
                        index.add(BaseData(
                                title = this.source[i],
                                url = this.source[i + 1],
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
