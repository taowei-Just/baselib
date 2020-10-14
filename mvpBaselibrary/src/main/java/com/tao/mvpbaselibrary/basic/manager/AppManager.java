package com.tao.mvpbaselibrary.basic.manager;

import android.app.Activity;

import java.util.Stack;


/**
 * @author chengang
 */
public class AppManager {

    /**
     * Activity堆栈
     */
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    /**
     * 私有构造方法
     */
    private AppManager() {
    }

    /**
     * 单一实例
     *
     * @return AppManager单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    public void goStackBottom() {
        if (getActivityStack() == null) {
            return;
        }
        for (int i = getActivityStack().size() - 1; i >= 1; i--) {
            Activity activity = getActivityStack().get(i);
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 获取Activity堆栈
     *
     * @return Activity堆栈
     */
    public static Stack<Activity> getActivityStack() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity Activity对象
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @return 当前Activity
     */
    public Activity currentActivity() {
        if (activityStack != null) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack != null) {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }


    /**
     * 结束指定的Activity
     *
     * @param activity Activity对象
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 弹栈
     *
     * @param activity 某个具体activity实例
     */
    public void popActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls Class对象
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 指定的Activity是否存在
     */
    public boolean isExist(Class<?> cls) {
        for (Activity activity : getActivityStack()) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }


}
