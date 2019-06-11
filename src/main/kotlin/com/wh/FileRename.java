package com.wh;

import java.io.File;

public class FileRename {

    static final String path = "C:\\Users\\wanghao\\Desktop\\bao\\附件";

    public static void main(String[] args){
        File file = new File(path);
        File[] dirs = file.listFiles();
        if(dirs == null)return;
        for(File dir:dirs){
            File[] emails = dir.listFiles();
            if(emails == null)continue;
            for(File email:emails){
                String name = email.getName()
                        .replace("2018","2019")
                        .replace("下","上");
                email.renameTo(new File(dir,name));
            }
        }
    }



}
