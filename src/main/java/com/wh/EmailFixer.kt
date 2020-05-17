package com.wh

import com.wh.utils.ZipUtils
import okio.Okio
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*
import kotlin.concurrent.thread


fun main() {
    val names = initNames()

    // 创建 JFrame 实例
    val frame = JFrame("小楠宝附件修改器")
    // Setting the width and height of frame
    frame.setSize(400, 500)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    val panel = JPanel()
    frame.add(panel)
    placeComponents(panel, names)
    frame.isVisible = true
}

private fun initNames(): Map<String, List<String>> {
    val source = Okio.buffer(Okio.source(File("./names_file")))

    val mappers = mutableMapOf<String, List<String>>()
    var values: MutableList<String>? = null
    var key: String

    while (true) {
        val line = source.readUtf8Line() ?: break
        if (line.isEmpty()) continue

        if (line.startsWith("#")) {
            key = line.substring(1)
            values = mutableListOf()
            mappers[key] = values
            continue
        }
        values?.add(line)
    }
    return mappers
}


private fun placeComponents(panel: JPanel, names: Map<String, List<String>>) {
    panel.layout = null
    //选择源文件
    val originTF = JTextField()
    originTF.setBounds(20, 20, 270, 25)
    panel.add(originTF)
    //选择文件按钮
    val selectOrigin = JButton("选择附件")
    selectOrigin.setBounds(300, 20, 80, 25)
    panel.add(selectOrigin)
    selectOrigin.addActionListener {
        val jfc = JFileChooser()
        jfc.fileSelectionMode = JFileChooser.FILES_ONLY
        jfc.isMultiSelectionEnabled = false
        jfc.showDialog(JLabel(), "选择")
        val file = jfc.selectedFile ?: return@addActionListener
        originTF.text = file.absolutePath
    }

    var outputDir: String? = null

    //选择输出路径
    val outputJt = JTextField()
    outputJt.setBounds(20, 75, 270, 25)
    panel.add(outputJt)

    val selectButton = JButton("输出路径")
    selectButton.setBounds(300, 75, 80, 25)
    panel.add(selectButton)
    selectButton.addActionListener {
        val jfc = JFileChooser()
        jfc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        jfc.isMultiSelectionEnabled = false
        jfc.showDialog(JLabel(), "选择")
        val file = jfc.selectedFile ?: return@addActionListener
        outputJt.text = file.absolutePath
        outputDir = file.absolutePath
    }

    //删除文件按钮
    val delButton = JButton("删除文件")
    delButton.setBounds(300, 110, 80, 25)
    panel.add(delButton)
    delButton.addActionListener {
        outputJt.text = null
        outputDir = null
    }

    val tipsInput = JLabel("输入要插入的内容：")
    tipsInput.setBounds(20, 180, 200, 25)
    panel.add(tipsInput)

    //创建输入label
    val userText = JTextArea()
    val inputScroll = JScrollPane(userText)
    inputScroll.setBounds(20, 205, 270, 100)
    panel.add(inputScroll)

    //创建输入label
    val logOutput = JTextArea("日志输出....\n")
    val logScroll = JScrollPane(logOutput)
    logScroll.setBounds(20, 320, 270, 100)
    panel.add(logScroll)


    val docxCb = JCheckBox("docx")
    docxCb.setBounds(300, 200, 80, 40)
    panel.add(docxCb)

    //开始修改按钮
    val goButton = JButton("开始修改")
    goButton.setBounds(300, 145, 80, 25)
    panel.add(goButton)
    goButton.addActionListener {
        if (outputDir == null) {
            JOptionPane.showMessageDialog(panel,
                    "输出文件夹为空！",
                    "小楠宝快看这里",
                    JOptionPane.WARNING_MESSAGE)
            return@addActionListener
        }
        if (userText.text == null || userText.text.isEmpty()) {
            JOptionPane.showMessageDialog(panel,
                    "小楠宝没有输入要插入的文字！",
                    "小楠宝快看这里",
                    JOptionPane.WARNING_MESSAGE)
            return@addActionListener
        }

        if (originTF.text.isEmpty()) {
            JOptionPane.showMessageDialog(panel,
                    "小楠宝没有选择附件！",
                    "小楠宝快看这里",
                    JOptionPane.WARNING_MESSAGE)
            return@addActionListener
        }

        val isDocx = docxCb.isSelected

        selectOrigin.isEnabled = false
        selectButton.isEnabled = false
        delButton.isEnabled = false
        goButton.isEnabled = false

        thread {
            //开启线程防止UI线程卡住
            try {
                startGo(outputDir!!, names, originTF.text, userText.text, logOutput, isDocx)
            } catch (e: Exception) {
                SwingUtilities.invokeLater { logOutput.append(e.message) }
            } finally {
                SwingUtilities.invokeLater {
                    //更新UI
                    logOutput.append("全部替换完成...\n")

                    selectOrigin.isEnabled = true
                    selectButton.isEnabled = true
                    delButton.isEnabled = true
                    goButton.isEnabled = true
                }
            }
        }
    }
}

fun startGo(outputDir: String,
            names: Map<String, List<String>>,
            originFile: String,
            insertText: String,
            logText: JTextArea,
            isDocx: Boolean) {
    //打开源文档
    val manager = MSWorldMgr(false)
    manager.setSaveOnExit(false)
    manager.openDocument(originFile)
    manager.moveEnd()
    for (i in 0 until 100) {
        manager.insertText("\n")
    }

    val docDirs = mutableListOf<File>()

    // 创建临时文件夹
    val docTemp = File(File(outputDir), "docs")

    // 开始生产文件
    for ((k, v) in names) {

        val docDir = File(docTemp, k)
        if (!docDir.exists()) docDir.mkdirs()

        docDirs.add(docDir)

        for (name in v) {
            manager.moveEnd()
            manager.insertText(insertText)

            val path = "${docDir.absolutePath}${File.separator}$name"

            if (isDocx) {
                manager.saveDocx("$path.docx")
            } else {
                manager.save("$path.doc")
            }

            SwingUtilities.invokeLater {
                //更新UI
                logText.append("替换 $name 完成...\n")
            }

        }

    }

    manager.closeDocument()
    manager.close()
    SwingUtilities.invokeLater {
        //更新UI
        logText.append("开始打包...\n")
    }

    startZipPacket(outputDir, docDirs, isDocx)

    // 删除临时文件
    docTemp.deleteRecursively()

    SwingUtilities.invokeLater {
        //更新UI
        logText.append("打包完成...\n")
    }
}


fun startZipPacket(zipFolder: String, srcDirList: MutableList<File>, isDocx: Boolean) {
    val format = SimpleDateFormat("yyyyMMddHHmm")
    val date = format.format(Date())
    val zipName = "附件${if (isDocx) "docx" else ""}$date.zip"
    val fos1 = FileOutputStream(File("$zipFolder/$zipName"))
    ZipUtils.toZip(srcDirList, fos1, true)
}


