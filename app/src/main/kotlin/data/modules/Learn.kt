package data.modules

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/12.
 * data model
 */

object Learn: Module() {
    override var link = "${parentLink}learn.txt"
    override var type = TYPE_OTHER_LIST
}
