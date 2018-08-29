package com.wh.socket

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import kotlin.concurrent.thread

class SvcClient {
    var mSocket: Socket? = null
    var mOut: OutputStream? = null
    var mIn: InputStream? = null

    fun connect() {
        mSocket = Socket("172.16.128.170", 8090)
        mOut = mSocket?.getOutputStream()
        mIn = mSocket?.getInputStream()
        runRecLoop(mSocket, mIn)
    }

    fun sendMsg(msg: String) {
        mOut?.write(msg.toByteArray(Charset.defaultCharset()))
        mOut?.flush()
    }

    private fun runRecLoop(socket: Socket?, ips: InputStream?) {
        val soc = socket ?: return
        val ipsV = ips ?: return
        thread {
            try {
                val buffer = ByteArray(11)
                while (true) {
                    val count = ipsV.read(buffer)
                    println("count = $count msg: ${String(buffer)}")
                }
            } catch (e: Exception) {
                print("socket exception: $e")
            } finally {
            }
        }
    }
}

fun main(args: Array<String>) {
    val client = SvcClient()
    client.connect()
    client.sendMsg("ABCDFSFSDFF")
    Thread.sleep(2000)
    client.sendMsg("124q33tvvfafasfFFF")

}