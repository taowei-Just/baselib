package com.tao.mvpbaselibrary.basic.manager.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tao.logger.log.Logger;


/**
 * @author chengang
 * @date 2018/7/12
 * @describe 检测app是否前台后台运行
 */
public class LifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    private static final String TAG = "MyLifecycleHandler";


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        if (applicationRunCallback != null) {
            if (isApplicationInForeground()) {
                applicationRunCallback.inForeground();
            } else {
                applicationRunCallback.inBackgound();
            }
        }
        Logger.i("onActivityResumed" + resumed + "---pause" + paused);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        if (applicationRunCallback != null) {
            if (isApplicationInForeground()) {
                applicationRunCallback.inForeground();
            } else {
                applicationRunCallback.inBackgound();
            }
        }
        Logger.i( "onActivityPaused" + paused + "---resume" + resumed);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        Logger.i("application is visible: " + (started > stopped));
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }

    public static boolean isApplicationInBackround() {
        Logger.i("resumed" + resumed + "----paused" + paused);
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于后台状态中
        return resumed <= paused;
    }

    public LifecycleHandler setApplicationRunCallback(Callback applicationRunCallback) {
        this.applicationRunCallback = applicationRunCallback;
        return this;
    }

    public static LifecycleHandler create() {
        return new LifecycleHandler();
    }

    Callback applicationRunCallback;

    public interface Callback {
        /**
         * 前台
         */
        void inForeground();

        /**
         * h后台
         */
        void inBackgound();

    }
}
