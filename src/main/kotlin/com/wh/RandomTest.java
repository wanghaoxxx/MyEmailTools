package com.wh;

import java.util.Random;

public class RandomTest {

    //j xiaomi
    //k smart os
    // 5局3胜
    public static void main(String[] args){
        int j=0,k=0;
        for(int i = 0;i<100;i++) {
            int rand =(int)(Math.random() * 100);
            if(rand>50){
                j++;
            }else {
                k++;
            }
        }
        System.out.println(j>k);
    }









}
