package data.modules

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/12.
 * data model
 */

object Blogs: Module() {
    override var link: String = "${parentLink}blogs.txt"
    override var num = NUMBER_TWO
    override var type = TYPE_LIST

    val appreciateLink = "${parentLink}thanks.txt"
}
