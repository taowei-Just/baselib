package com.tao.mvpbaselibrary.basic.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {


    @Deprecated
    public static void show(Context context, int resId) {
        show(resId);
    }

    @Deprecated
    public static void shortShow(Context context, int resId) {
        shortShow(resId);
    }

    @Deprecated
    public static void show(Context context, String msg) {
        show(msg);
    }

    @Deprecated
    public static void shortShow(Context context, String msg) {
        shortShow(msg);

    }


    public static void show(int resId) {
        Toast.makeText(initContext, initContext.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void shortShow(int resId) {
        Toast.makeText(initContext, initContext.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void show(String msg) {
        Toast.makeText(initContext, msg, Toast.LENGTH_LONG).show();
    }

    public static void shortShow(String msg) {
        Toast.makeText(initContext, msg, Toast.LENGTH_SHORT).show();
    }

    private static Context initContext;
    
    public  static  void init(Context context){
        initContext =context;
    }

  
}
