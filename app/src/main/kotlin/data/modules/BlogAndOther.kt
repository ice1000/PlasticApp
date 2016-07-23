package data.modules

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/12.
 * data model
 */

object BlogAndOther : Module() {
    override var link: String = "${parentLink}blogs.txt"
    override var type = TYPE_LIST

    val appreciateLink = "${parentLink}thanks.txt"
    val qqLink = "${parentLink}qq.txt"
}
