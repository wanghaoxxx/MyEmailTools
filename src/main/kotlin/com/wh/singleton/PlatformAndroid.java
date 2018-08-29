package com.wh.singleton;

public class PlatformAndroid {

    private static final Singleton<PlatformAndroid> mAndroidPlatform = new Singleton<PlatformAndroid>() {
        @Override
        PlatformAndroid create() {
            return new PlatformAndroid();
        }
    };

    public static void main(String[] args) {
        mAndroidPlatform.get().printMsg();
        mAndroidPlatform.get().printMsg();
    }

    private void printMsg() {
        System.out.println(this + " PlatformAndroid  == printMsg  classloader = " + getClass().getClassLoader());

    }


}
