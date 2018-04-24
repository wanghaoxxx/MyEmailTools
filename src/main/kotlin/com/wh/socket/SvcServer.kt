package com.wh.socket

import okio.Okio
import java.io.InputStream
import java.net.ServerSocket
import java.util.concurrent.Executors

class SvcServer {

    private val mPools = Executors.newFixedThreadPool(5)

    fun main(args: Array<String>) {
        startSocServer()
    }

    private fun startSocServer() {
        val server = ServerSocket(12345)
        while (true) {
            val clientSock = server.accept()
            println("收到客户端请求：Ip : " + clientSock.inetAddress)
            mPools.submit {
                val inStream = clientSock.getInputStream()
                handleClientRequest(inStream)
            }
        }
    }

    private fun handleClientRequest(ins: InputStream) {
        val buffer = Okio.buffer(Okio.source(ins))
        val msg = buffer.readUtf8Line()
        println("收到客户端信息 ：" + msg)
    }

}