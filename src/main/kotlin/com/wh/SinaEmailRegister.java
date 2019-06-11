package com.wh;

import okhttp3.*;

public class SinaEmailRegister {

    public static void main(String[] args){
        OkHttpClient client = new OkHttpClient.Builder().build();

        RequestBody body = new FormBody.Builder().add("email","hsmdmmm27")
                .add("fpsw","wwwwhh").add("npsw","wwwwhh")
                .add("phonenumber","13689639479")
                .add("pswqa_a","639479").add("checkbox1","1").build();

        Request request = new Request.Builder().addHeader(":authority","mail.sina.com.cn")
                .addHeader(":method","POST").addHeader(":path","")
                .addHeader(":scheme","").addHeader("cookie","U_TRS1=000000ac.4444a271.5c846c1d.74cef861; UOR=,mail.sina.com.cn,; SINAGLOBAL=172.16.7.68_1552182301.131895; U_TRS2=000000ac.8a8a6dba.5c879ebe.dcb02b4b; PHPSESSID=7dll0k2gfmtdoraqvmh24ajag6; SMCHECKMAIL=6962b0878ffe9b0b19451fc7756d0f1c; WEB2=57dab1382fe02d4d7ed4ecd269b8ac6b; ULV=1552391873718:3:3:3::1552264772544; Apache=10.13.240.96_1552391871.138238")
                .addHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
                .post(body).url("https://mail.sina.com.cn/register/reg_vipmail.php").build();

        try {
          Response response =  client.newCall(request).execute();

          System.out.println("response : "+response.body().string());
        }catch (Exception e){


        }




    }




}
