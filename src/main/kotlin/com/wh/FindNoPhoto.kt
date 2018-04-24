package com.wh

import com.alibaba.fastjson.JSON
import java.io.File
import jxl.Workbook
import okio.Okio


data class RowModel(var title: String? = null,
                    var step: String? = null,
                    var result: String? = null)

fun main(args: Array<String>) {
    val path = "/Users/wanghao/Downloads/主流程测试用例.xls"
    val xlsFile = File(path)
    // 获得工作簿对象
    val workbook = Workbook.getWorkbook(xlsFile)
    // 获得所有工作表
    val sheets = workbook.sheets
    // 遍历工作表params
    if (sheets != null) {
        for (sheet in sheets) {
            // 获得行数
            val rows = sheet.rows
            val list = mutableListOf<RowModel>()
            // 读取数据
            for (row in 0 until rows) {
                val model = RowModel()
                model.title = sheet.getCell(0, row).contents
                model.step = sheet.getCell(1, row).contents
                model.result = sheet.getCell(2, row).contents
                list.add(model)
            }
            val json = JSON.toJSONString(list)
            println(json)
            saveJsonFile(json, sheet.name)
        }
    }
    workbook.close()
}

fun saveJsonFile(json: String, name: String) {
    val path = "/Users/wanghao/Downloads/"
    val sink = Okio.buffer(Okio.sink(File("$path$name.txt")))
    sink.writeUtf8(json)
    sink.flush()
    sink.close()
}