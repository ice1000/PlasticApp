package data.modules

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/12.
 * data model
 */

object News: Module(){
    override var link = "${parentLink}index.txt"
    override var num = NUMBER_THREE
    override var type = TYPE_LIST
}
