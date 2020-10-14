package com.tao.mvpbaselibrary.basic.rxlife;

import org.reactivestreams.Subscriber;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOperator;
import io.reactivex.FlowableOperator;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOperator;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOperator;

/**
 * User: ljx
 * Date: 2019/4/5
 * Time: 13:46
 */
public final class RxLifeOperator<T> implements
        FlowableOperator<T, T>,
        ObservableOperator<T, T>,
        SingleOperator<T, T>,
        MaybeOperator<T, T>,
        CompletableOperator {

    private Scope scope;

    RxLifeOperator(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Subscriber<? super T> apply(Subscriber<? super T> subscriber) throws Exception {
        return new LifeSubscriber<>(subscriber, scope);
    }

    @Override
    public Observer<? super T> apply(Observer<? super T> observer) throws Exception {
        return new LifeObserver<>(observer, scope);
    }

    @Override
    public SingleObserver<? super T> apply(SingleObserver<? super T> observer) throws Exception {
        return new LifeSingleObserver<>(observer, scope);
    }

    @Override
    public MaybeObserver<? super T> apply(MaybeObserver<? super T> observer) throws Exception {
        return new LifeMaybeObserver<>(observer, scope);
    }

    @Override
    public CompletableObserver apply(CompletableObserver observer) throws Exception {
        return new LifeCompletableObserver(observer, scope);
    }

}
