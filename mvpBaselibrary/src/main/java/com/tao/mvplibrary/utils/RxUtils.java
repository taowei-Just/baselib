package com.tao.mvplibrary.utils;


import android.os.Looper;

import androidx.lifecycle.Lifecycle;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.ObservableSubscribeProxy;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    /**
     * 订阅
     *
     * @param observable    被观察者
     * @param observer      观察者
     * @param lifecycle     使用生命周期自动接绑  可为null
     * @param observeOnMain
     * @param <T>
     */
    public static <T> void toSubscribe(Observable<T> observable, Observer<? super T> observer, Lifecycle lifecycle, boolean subscribeOnMain, boolean observeOnMain) {
        if (lifecycle == null) {
            getEntityObservable(observable, subscribeOnMain, observeOnMain).subscribe(observer);
        } else {
            ((ObservableSubscribeProxy) observable.compose(getComposer(subscribeOnMain, observeOnMain)).as(bindLifecycle(lifecycle))).subscribe(observer);
        }
    }

    /**
     * 绑定 生命周期 默认观察者为main线程
     *
     * @param observable
     * @param resultObserver
     * @param lifecycle
     * @param <T>
     */
    public static <T> void toSubscribe(Observable<T> observable, Observer<T> resultObserver, Lifecycle lifecycle) {
        toSubscribe(observable, resultObserver, lifecycle, false, true);
    }

    public static <T> void toSubscribe(Observable<T> observable, Observer<T> resultObserver, Lifecycle lifecycle, boolean observeOnMain) {
        toSubscribe(observable, resultObserver, lifecycle, false, observeOnMain);
    }

    /**
     * 不绑定 生命周期 默认观察者为main线程
     *
     * @param observable
     * @param resultObserver
     * @param <T>
     */
    public static <T> void toSubscribe(Observable<T> observable, Observer<T> resultObserver) {
        toSubscribe(observable, resultObserver, false, true);
    }


    /**
     * 不绑定 生命周期  自由设置观察者线程
     *
     * @param observable
     * @param resultObserver
     * @param <T>
     */
    public static <T> void toSubscribe(Observable<T> observable, Observer<T> resultObserver, boolean obserMain) {
        toSubscribe(observable, resultObserver, false, obserMain);
    }

    public static <T> void toSubscribe(Observable<T> observable, Observer<T> resultObserver, boolean subscribeOnMain, boolean obserMain) {
        toSubscribe(observable, resultObserver, null, subscribeOnMain, obserMain);
    }


    /**
     * 绑定到lifecycle
     *
     * @param lifecycle
     * @param <T>
     * @return
     */
    public static <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle lifecycle) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_DESTROY));
    }

    /**
     * 线程调度器
     *
     * @param <T>
     * @param subscribeOnMain
     * @param observeOnMain
     * @return
     */
    public static <T> ObservableTransformer<T, T> getComposer(final boolean subscribeOnMain, final boolean observeOnMain) {
        return new ObservableTransformer<T, T>() {
            public ObservableSource<T> apply(Observable<T> upstream) {
                if (subscribeOnMain) {
                    upstream.subscribeOn(AndroidSchedulers.mainThread());
                } else {
                    if (Thread.currentThread().getName().equals(Looper.getMainLooper().getThread().getName())) {
                        upstream.subscribeOn(Schedulers.io());
                    }
                }
                if (observeOnMain) {
                    return upstream.observeOn(AndroidSchedulers.mainThread());
                } else {
                    return upstream.observeOn(Schedulers.io());
                }
            }
        };
    }

    /**
     * 是否切换到UI线程
     *
     * @param <T>
     * @param observable
     * @param subscribeOnMain
     * @param observeOnMain
     * @return
     */
    private static <T> Observable<T> getEntityObservable(Observable<T> observable, boolean subscribeOnMain, boolean observeOnMain) {

        if (subscribeOnMain) {
            observable.subscribeOn(AndroidSchedulers.mainThread());
        } else {
            if (Thread.currentThread().getName().equals(Looper.getMainLooper().getThread().getName())) {
                observable.subscribeOn(Schedulers.io());
            }
        }

        return observeOnMain ? observable.observeOn(AndroidSchedulers.mainThread()) : observable.observeOn(Schedulers.io());
    }

}
