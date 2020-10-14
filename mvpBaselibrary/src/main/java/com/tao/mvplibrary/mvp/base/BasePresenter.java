package com.tao.mvplibrary.mvp.base;


import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.tao.mvpbaselibrary.mvp.base.BaseObserver;
import com.tao.mvplibrary.mvp.IModle;
import com.tao.mvplibrary.mvp.IPresenter;
import com.tao.mvplibrary.mvp.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2019-8-7.
 */

public abstract class BasePresenter<V extends IView, M extends IModle> implements IPresenter<V> {
    public String tag = getClass().getSimpleName();
    private boolean deattachV = false;
    private WeakReference<V> v;
    public M mModle;

    public boolean isDeattachV() {
        return deattachV;
    }

    public BasePresenter() {
        EventBus.getDefault().register(this);
        setM(creatM());
        setLifecycle(getLifecycle());
    }

    public M creatM() {
        return null;
    }

    @Override
    public V getV() throws Exception {
        if (!isAttachedV())
            throw new Exception("v is null or v un attached");

        return v.get();
    }

    public boolean isAttachedV() {
        return v != null && v.get() != null;
    }

    public void setM(M modle) {
        mModle = modle;
    }

    final public M getM() {
        return mModle;
    }

    public void attachView(V view) {
        if (this.v != null)
            v.clear();
        this.v = new WeakReference<>(view);
        deattachV = false;
    }

    public void dettachView() {
        deattachV = true;
        EventBus.getDefault().unregister(this);
        if (null != mModle)
            mModle.deattach();
        mModle = null;
        if (null != v && null != v.get())
            v.clear();
        v = null;
    }

    /**
     * 增加lifecycle 参数传入
     */
    WeakReference<Lifecycle> lifecycle;

    public Lifecycle getLifecycle() {
        Lifecycle lf = null;
        if (lifecycle == null) {
            lf = castLifecycle();
        } else if (null != lifecycle.get()) {
            lf = lifecycle.get();
        } else {
            lf = castLifecycle();
        }
        return lf;
    }

    private Lifecycle castLifecycle() {
        Lifecycle lf = null;
        if (v == null || v.get() == null)
            return lf;

        if ((v.get() instanceof Lifecycle)) {
            lf = (Lifecycle) v.get();
        } else if ((v.get() instanceof LifecycleRegistry)) {
            lf = (LifecycleRegistry) v.get();
        } else if ((v.get() instanceof LifecycleOwner)) {
            lf = v.get().getLifecycle();
        }
        return lf;
    }

    public void setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    @Subscribe
    public void sub(String str) {
    }

    // 抛到ui执行
    public void runOnUI(final Runnable runnable) {
        runThread(runnable, true);
    }


    public void runThread(final Runnable runnable, boolean observerOnMain) {

        final Disposable[] disposable = new Disposable[1];
        try {
            Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(0);
                }
            });
            if (observerOnMain)
                observable.observeOn(AndroidSchedulers.mainThread());
            else
                observable.observeOn(Schedulers.io());

            observable.subscribe(new BaseObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                    super.onSubscribe(d);
                    disposable[0] = d;
                }

                @Override
                protected void accept(Object o) {
                    runnable.run();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    Log.e(tag, "onError");
                    closeDispos(disposable[0]);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    closeDispos(disposable[0]);
                    Log.e(tag, "onComplete");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDispos(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


}
