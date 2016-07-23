package data.modules

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/12.
 */


abstract class Module {

    protected val parentLink =
            "https://coding.net/u/ice1000/p/pastic-raw/git/raw/master/raw/"

    companion object {
        val TYPE_LIST = 0xFF1
        val TYPE_OTHER_LIST = 0xFF2

        val TYPE_FLOW = 0xFF3
    }

    abstract var link: String
    abstract var type: Int

}
