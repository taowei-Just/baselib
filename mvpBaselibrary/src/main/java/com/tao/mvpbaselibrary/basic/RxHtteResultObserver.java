package com.tao.mvpbaselibrary.basic;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class RxHtteResultObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T t) {
        if (t==null){
            onFault("result is null");
        }else {
            onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof SocketTimeoutException) {//请求超时
                onFault("请求超时");
            } else if (e instanceof ConnectException) {//网络连接超时
                onFault("网络连接超时");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                onFault("安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    onFault("网络异常,请检查您的网络状态");
                } else if (code == 404) {
                    onFault("请求的地址不存在");
                } else {
                    onFault("请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                onFault("网络错误,请稍后再试");
            } else {
                onFault("error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            onFault("error:" + e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    public void onFault(String error) {
    }
    public void onSuccess(T t) {
    
        
    }
}
