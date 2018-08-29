package com.wh.singleton;

public abstract class Singleton<T> {
    private volatile T mInstance;

    abstract T create();

    public final T get() {
        if (mInstance == null) {
            synchronized (this) {
                if (mInstance == null) {
                    mInstance = create();
                }
            }
        }
        return mInstance;
    }

}
