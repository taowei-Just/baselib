package com.tao.mvpbaselibrary.basic.rxlife;

import io.reactivex.CompletableConverter;
import io.reactivex.FlowableConverter;
import io.reactivex.MaybeConverter;
import io.reactivex.ObservableConverter;
import io.reactivex.SingleConverter;
import io.reactivex.parallel.ParallelFlowableConverter;

/**
 * User: ljx
 * Date: 2019/4/18
 * Time: 18:40
 */
public interface RxConverter<T> extends
        ObservableConverter<T, ObservableLife<T>>,
        FlowableConverter<T, FlowableLife<T>>,
        ParallelFlowableConverter<T, ParallelFlowableLife<T>>,
        MaybeConverter<T, MaybeLife<T>>,
        SingleConverter<T, SingleLife<T>>,
        CompletableConverter<CompletableLife> {
}
