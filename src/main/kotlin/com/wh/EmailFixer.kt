package com.wh

import java.io.File
import javax.swing.*
import javax.swing.JList
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    // 创建 JFrame 实例
    val frame = JFrame("小楠宝附件修改器")
    // Setting the width and height of frame
    frame.setSize(400, 500)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    val panel = JPanel()
    frame.add(panel)
    placeComponents(panel)
    frame.isVisible = true
}

private fun placeComponents(panel: JPanel) {
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
        val file = jfc.selectedFile
        if (file == null) {
            JOptionPane.showMessageDialog(panel,
                    "小楠宝不要忘记选择文件哦！",
                    "小楠宝快看这里",
                    JOptionPane.WARNING_MESSAGE)
            return@addActionListener
        }

        originTF.text = file.absolutePath
    }


    //选择文件列表控件
    val scrollPane = JScrollPane()
    scrollPane.setBounds(20, 65, 270, 120)
    val listItem = DefaultListModel<String>()
    listItem.addElement("选择文件...")
    val list = JList(listItem)

    scrollPane.setViewportView(list)
    panel.add(scrollPane)

    //选择文件按钮
    val selectButton = JButton("选择文件")
    selectButton.setBounds(300, 75, 80, 25)
    panel.add(selectButton)
    selectButton.addActionListener {
        val jfc = JFileChooser()
        jfc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        jfc.isMultiSelectionEnabled = true
        jfc.showDialog(JLabel(), "选择")
        val files = jfc.selectedFiles
        if (files == null || files.isEmpty()) {
            JOptionPane.showMessageDialog(panel,
                    "小楠宝不要忘记选择文件哦！",
                    "小楠宝快看这里",
                    JOptionPane.WARNING_MESSAGE)
            return@addActionListener
        }

        listItem.clear()

        files.forEach {
            listItem.addElement(it.absolutePath)
        }
        list.model = listItem
    }

    //删除文件按钮
    val delButton = JButton("删除文件")
    delButton.setBounds(300, 110, 80, 25)
    panel.add(delButton)
    delButton.addActionListener {
        val selected = list.selectedIndex
        if (selected < 0) {
            JOptionPane.showMessageDialog(panel,
                    "小楠宝没有选要删那个哦！",
                    "小楠宝快看这里",
                    JOptionPane.WARNING_MESSAGE)
            return@addActionListener
        }

        if (selected < listItem.size()) {
            listItem.remove(selected)
            list.model = listItem
        }
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


    //开始修改按钮
    val goButton = JButton("开始修改")
    goButton.setBounds(300, 145, 80, 25)
    panel.add(goButton)
    goButton.addActionListener {
        if (listItem.size() == 0) {
            JOptionPane.showMessageDialog(panel,
                    "小楠宝没选替换文件夹！",
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

        selectOrigin.isEnabled = false
        selectButton.isEnabled = false
        delButton.isEnabled = false
        goButton.isEnabled = false

        thread {
            //开启线程防止UI线程卡住
            try {
                startGo(listItem, originTF.text, userText.text, logOutput)
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

fun startGo(listModel: DefaultListModel<String>,
            originFile: String,
            insertText: String,
            logText: JTextArea) {
    //打开源文档
    val manager = MSWordManager(false)
    manager.setSaveOnExit(false)
    manager.openDocument(originFile)
    manager.moveEnd()
    for (i in 0 until 100) {
        manager.insertText("\n")
    }
    for (i in 0 until listModel.size()) {
        val replaceFolder = File(listModel.getElementAt(i))
        val replaces = replaceFolder.listFiles() ?: return
        for (replace in replaces) {
            if (!replace.name.endsWith(".doc")) continue
            manager.moveEnd()
            manager.insertText(insertText)
            manager.save(replace.absolutePath)
            SwingUtilities.invokeLater {
                //更新UI
                logText.append("替换 ${replace.absolutePath} 完成...\n")
            }
        }
    }
    manager.closeDocument()
    manager.close()
}





