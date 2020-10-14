package com.tao.mvpbaselibrary.lekcanary;

import android.content.Context;


public class LeakCanaryHelper {
    private static LeakCanaryWrapperImpl canaryWrapper;

    public static void init() {
        canaryWrapper = new LeakCanaryWrapperImpl();
    }

    public static void witch(Object o) {
//        if (canaryWrapper == null)
//            canaryWrapper.watch(o);
    }

    public static LeakCanaryWrapperImpl getLeakCanary(Context context) {
        if (context == null || context.getApplicationContext() == null) {
            return null;
        }
        return canaryWrapper;
    }
}
