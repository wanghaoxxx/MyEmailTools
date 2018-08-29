package com.wh.thread;

import java.io.FileInputStream;

public class ThreadTest {

    public static void main(String[] args) {
        System.out.println("yield start time = " + System.currentTimeMillis());

        new Thread(new Runnable() {
            public void run() {
                try {
                    FileInputStream fis = new FileInputStream("/Users/wanghao/Desktop/123.png");
                    byte[] buffer = new byte[256];
                    byte[] bytes = new byte[0];

                    while (fis.read(buffer) != -1) {
                        final int lastLength = bytes.length;
                        bytes = new byte[lastLength + buffer.length];
                        System.arraycopy(buffer, 0, bytes, lastLength, buffer.length);
                    }
                    System.out.println("bytes length = " + bytes.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Thread end time = " + System.currentTimeMillis());
            }
        }).start();
        Thread.yield();
        System.out.println("main yield end time = " + System.currentTimeMillis());
    }

}

