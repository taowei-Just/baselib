package com.tao.mvpbaselibrary.basic.utils;

/**
 * Created by Administrator on 2016/1/8.
 */
public class ClickUtils {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }




}
