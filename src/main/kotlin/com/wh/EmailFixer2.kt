package com.wh

import java.io.File


const val PATH = "C:\\Users\\wanghao\\Desktop\\附件"

class EmailFixer2 {

    fun fix(){
        val file = File(PATH)

        file.listFiles().forEach {
            val docs = it.listFiles()

            docs.forEach { doc ->
                val name = doc.name.replace("2018","2019").replace("下","上")
                doc.renameTo(File(it,name))
            }
        }
    }



}