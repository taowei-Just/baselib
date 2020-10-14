package com.tao.mvpbaselibrary.basic.rxlife;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

/**
 * User: ljx
 * Date: 2019/4/5
 * Time: 14:06
 */
public interface RxLifeTransformer<T> extends
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        SingleTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {
}
