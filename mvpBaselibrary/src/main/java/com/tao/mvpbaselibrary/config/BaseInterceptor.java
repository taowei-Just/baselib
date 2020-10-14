package com.tao.mvpbaselibrary.config;

import android.graphics.Interpolator;

public class BaseInterceptor extends Interpolator {
    public BaseInterceptor(int valueCount) {
        super(valueCount);
    }

    public BaseInterceptor(int valueCount, int frameCount) {
        super(valueCount, frameCount);
    }
}
