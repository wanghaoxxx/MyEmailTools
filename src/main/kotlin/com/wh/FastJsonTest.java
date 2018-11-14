package com.wh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

public class FastJsonTest {

    private static final String Json = "{\"code\":1,\"res\":{\"name\":\"wanghao\",\"age\":\"15\",\"classes\":[{\"yuwen\":\"80\",\"shuxue\":\"99\",\"gaoshu\":\"222\"},{\"yuwen\":\"80\",\"shuxue\":\"99\"}]}}";

    static class AbsFastResp<T> {
        public int code;
        public T res;
    }


    static class StudentRes {
        public String name;
        public String age;
        public List<Classes> classes;

        @Override
        public String toString() {
            return "name :" + name + " age : " + age;
        }
    }

    static class Classes {
        public String yuwen;
        public String shuxue;
        public String gaoshu;
    }

    public static void main(String[] args) {
        JSONObject ib = JSON.parseObject(Json);
        AbsFastResp<StudentRes> resp = new AbsFastResp<StudentRes>();
        resp.code = ib.getIntValue("code");
        JSONObject resObj = ib.getJSONObject("res");
        resp.res = JSON.toJavaObject(resObj, StudentRes.class);

        System.out.println("resp = " + resp.code + " resp res = " + resp.res);
    }

}
