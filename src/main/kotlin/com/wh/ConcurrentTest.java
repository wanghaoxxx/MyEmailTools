package com.wh;


import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentTest {


    public static void main(String[] args) {
        final ConcurrentTest test = new ConcurrentTest();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        test.inc.incrementAndGet();
                    }
                    System.out.println(test.inc.get());
                }
            }).start();
        }
    }

    private volatile AtomicInteger inc = new AtomicInteger();

}
