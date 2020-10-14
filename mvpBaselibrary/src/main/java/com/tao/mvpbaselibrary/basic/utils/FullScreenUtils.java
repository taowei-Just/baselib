package com.tao.mvpbaselibrary.basic.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class FullScreenUtils {

    /**
     * 非全面屏下 虚拟键实际高度(隐藏后高度为0)
     *
     * @param context
     * @return
     */
    public static int getCurrentNavigationBarHeight(Context context) {
        if (isNavigationBarShown(context))
            return getNavigationBarHeight(context);
        else
            return 0;
    }

    /**
     * 非全面屏下 虚拟键高度(无论是否隐藏)
     *
     * @param context
     * @return
     */
    private static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 非全面屏下 虚拟按键是否打开
     *
     * @param context
     * @return
     */
    private static boolean isNavigationBarShown(Context context) {
        //虚拟键的view,为空或者不可见时是隐藏状态
        Activity activity = (Activity) context;
        View view = activity.findViewById(android.R.id.navigationBarBackground);
        if (view == null) {
            return false;
        }
        int visible = view.getVisibility();
        if (visible == View.GONE || visible == View.INVISIBLE) {
            return false;
        } else {
            return true;
        }
    }


}
