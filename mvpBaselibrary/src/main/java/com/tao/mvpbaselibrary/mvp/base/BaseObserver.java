package com.tao.mvpbaselibrary.mvp.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {
    
    @Override
    public void onSubscribe(Disposable d) {
    
    }

    @Override
    public void onNext(T o) {
    accept(o);
    }

    protected abstract void accept(T o);

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
    
    
}
