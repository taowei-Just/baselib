package com.tao.mvpbaselibrary.basic.manager.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;


import com.tao.logger.log.Logger;

import java.lang.ref.WeakReference;

/**
 * @author chengang
 * @date  
 * @email  
 * @package_name com.nj.baijiayun.module_public.manager
 * @describe
 */
public class LifecycleManager {

    private WeakReference<LifecycleOwner> lifecycleOwner;

    public static LifecycleManager create(LifecycleOwner lifecycleOwner) {
        LifecycleManager lifecycleManager = new LifecycleManager();
        lifecycleManager.lifecycleOwner = new WeakReference<>(lifecycleOwner);
        return lifecycleManager;
    }

    public void addLifecycleCallBack(BaseObserver baseObserver) {
        if (this.lifecycleOwner == null || this.lifecycleOwner.get() == null) {
            return;
        }
        this.lifecycleOwner.get().getLifecycle().addObserver(baseObserver);

    }

    public static abstract class BaseObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void onCreate() {
            Logger.i("onCreate");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onStart() {
            Logger.i("onStart");

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
            Logger.i("onResume");

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause() {
            Logger.i("onPause");

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onStop() {
            Logger.i("onStop");

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestory() {
            Logger.i("onDestory");
        }
    }


}
