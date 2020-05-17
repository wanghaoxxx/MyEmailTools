package com.wh

import org.jsoup.Jsoup
import java.io.File
import java.util.*

class UnusedResourceRemover {

    fun main(args: Array<String>) {
        val jsoup = Jsoup.parse(File("/Users/wanghao/Desktop/lint-results.xml"), "UTF-8")
        val elements = jsoup.select("issue")

        elements.filter { element ->
            element.attr("id") == "UnusedResources"
        }.map {
            it.select("location")[0].attr("file")
        }.filter { !it.contains("umeng") && (it.contains("drawable") || it.contains("layout")) }.filter {
            println(it)
            true
        }.forEach {
            File(it).delete()
        }
    }
}