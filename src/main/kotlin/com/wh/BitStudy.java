package com.wh;


import okio.BufferedSink;
import okio.Okio;

import java.io.File;

public class BitStudy {

    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\wanghao\\Desktop\\附件";

        String nameFile = ".\\names_file";

        BufferedSink sink = Okio.buffer(Okio.appendingSink(new File(nameFile)));


        File file = new File(path);

        File[] children = file.listFiles();
        if (children == null) return;

        for (File f : children) {
            File[] ffs = f.listFiles();
            if (ffs == null) continue;

            for (File ff : ffs) {
                sink.writeUtf8(ff.getName().replace(".doc", "")).writeUtf8("\n");
            }
        }
        sink.flush();
        sink.close();
    }

}
