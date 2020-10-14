package com.tao.mvpbaselibrary.lib_http.retrofit;

import android.util.Log;

import com.tao.mvpbaselibrary.lib_http.retrofit.retry.RetryWrapper;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class NetInterceptor implements Interceptor {
    private static final String TAG = "NetInterceptor";
    private NetConfigProvider defaultProvider;
    /**
     * 最大重试次数
     */
    private int maxRetry = 3;
    /**
     * 假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
     */
    //    延迟
    private long delay = 3000;
    //    叠加延迟
    private long increaseDelay = 5000;

    public NetInterceptor(NetConfigProvider provider) {
        this.defaultProvider = provider;

    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        chain = handlerTimeOut(request, chain);

        if (defaultProvider.configHandler() != null) {
            request = defaultProvider.configHandler().handleRequest(request, chain);
        }
        if (this.defaultProvider != null) {
            this.maxRetry = this.defaultProvider.configMaxRetry();
        }

        RetryWrapper retryWrapper = proceed(chain, request);
        while (retryWrapper.isNeedReTry()) {
            retryWrapper.retryNum++;
            Log.d(TAG, MessageFormat.format("url={0} retryNum= {1}", retryWrapper.request.url().toString(), retryWrapper.retryNum));
            try {
                Thread.sleep(delay + (retryWrapper.retryNum - 1) * increaseDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            proceed(chain, retryWrapper.request, retryWrapper);
        }


        if (defaultProvider != null && defaultProvider.configHandler() != null) {
            retryWrapper.response = defaultProvider.configHandler().handleResponse(retryWrapper.response, chain);
        }
        return retryWrapper.response == null ? chain.proceed(request) : retryWrapper.response;
    }

    private Chain handlerTimeOut(Request request, Chain chain) {
        Log.e(TAG, request.headers().toString());
        String connectTimeOut = request.header(NetManagerHeaderConfig.CONNECT_TIME_OUT_MILLISECONDS);
        String readTimeOut = request.header(NetManagerHeaderConfig.READ_TIME_OUT_MILLISECONDS);
        String writeTimeOut = request.header(NetManagerHeaderConfig.WRITE_TIME_OUT_MILLISECONDS);
        String timeOut = request.header(NetManagerHeaderConfig.TIME_OUT_MILLISECONDS);
        try {
            if (timeOut != null) {
                chain = chain.withConnectTimeout(Integer.parseInt(timeOut), TimeUnit.MILLISECONDS)
                        .withWriteTimeout(Integer.parseInt(timeOut), TimeUnit.MILLISECONDS)
                        .withReadTimeout(Integer.parseInt(timeOut), TimeUnit.MILLISECONDS);
                return chain;
            }
            if (connectTimeOut != null) {
                chain = chain.withConnectTimeout(Integer.parseInt(connectTimeOut), TimeUnit.MILLISECONDS);
            }
            if (readTimeOut != null) {
                chain = chain.withReadTimeout(Integer.parseInt(readTimeOut), TimeUnit.MILLISECONDS);
            }
            if (writeTimeOut != null) {
                chain = chain.withWriteTimeout(Integer.parseInt(writeTimeOut), TimeUnit.MILLISECONDS);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return chain;

    }


    private RetryWrapper proceed(Chain chain, Request request) throws IOException {
        RetryWrapper retryWrapper = new RetryWrapper(request, maxRetry);
        proceed(chain, request, retryWrapper);
        return retryWrapper;
    }

    private void proceed(Chain chain, Request request, RetryWrapper retryWrapper) throws IOException {
        try {
            Response response = chain.proceed(request);
            retryWrapper.setResponse(response);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "Exception----->" + e.getMessage());
        }
    }


}
