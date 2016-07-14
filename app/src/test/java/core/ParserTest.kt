package core

import data.modules.Learn
import org.junit.Test

/**
 * @author ice1000
 * Created by ice1000 on 2016/7/14.
 */
class ParserTest {

    @Test
    fun parse() {
        val testSource = """
def path my_name_is_van

====
title
%path%i_am_an_artist
I'm hired from people to perfume their fantasies.
        """
        val returnObject = Parser.parse(testSource.split("\n"), Learn.type, Learn.num)
        returnObject.forEach {
            assert(it.title.equals("title"))
            println("url = ${it.url}")
            println("des = ${it.description}")
        }
    }

}
