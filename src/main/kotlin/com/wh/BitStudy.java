package com.wh;


import okio.BufferedSink;
import okio.Okio;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class BitStudy {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("172.16.96.50", 12345);
        OutputStream os = socket.getOutputStream();
        BufferedSink sink = Okio.buffer(Okio.sink(os));
        sink.writeUtf8("我是客户端001" + "\r\n");
        sink.flush();
    }
}
