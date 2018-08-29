package com.wh;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockerTest {

    public static void main(String[] args) {
        final Bank bank = new Bank();

        Thread transferT1 = new Thread(new Runnable() {
            public void run() {
                bank.transfer("laopo", "wanghao", 60000d);
            }
        });
        transferT1.start();
        try {
            Thread.sleep(2 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread putInT2 = new Thread(new Runnable() {
            public void run() {
                bank.putIn("laopo", 6000d);
            }
        });
        putInT2.start();
    }

    private static class Bank {
        private final ReentrantLock mBankLock;
        private final Condition mCondition;

        private static final Map<String, Double> mData = new HashMap<String, Double>();

        static {
            mData.put("wanghao", 5555555.0d);
            mData.put("laopo", 55555.0d);
        }

        Bank() {
            mBankLock = new ReentrantLock();
            mCondition = mBankLock.newCondition();
        }

        private void transfer(String from, String to, double amount) {
            mBankLock.lock();
            try {
                while (mData.get(from) < amount) { //自己的钱不够，等待中
                    System.out.println("money not enough, waiting for putIn");
                    mCondition.await();
                }
                //处理转账信息
                System.out.println("transfer success");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mBankLock.unlock();
            }
        }

        private void putIn(String account, double amount) {
            mBankLock.lock();
            try {
                double current = mData.get(account);
                mData.put(account, current + amount);
                mCondition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mBankLock.unlock();
            }
        }
    }


}
