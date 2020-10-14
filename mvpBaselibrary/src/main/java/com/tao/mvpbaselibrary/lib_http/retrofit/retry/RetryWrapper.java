package com.tao.mvpbaselibrary.lib_http.retrofit.retry;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author chengang
 * @date 2019-07-10
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.lib_http.retrofit.retry
 * @describe
 */
public class RetryWrapper {
    //假如设置为3次重试的话，则最大可能请求5次（默认1次+3次重试 + 最后一次默认）
    public volatile int retryNum = 0;
    public Request request;
    public Response response;
    private int maxRetry;

    public RetryWrapper(Request request, int maxRetry) {
        this.request = request;
        this.maxRetry = maxRetry;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    Response response() {
        return this.response;
    }

    Request request() {
        return this.request;
    }

    public boolean isSuccessful() {
        return response != null && response.isSuccessful();
    }

    public boolean isNeedReTry() {
        return needRetry(response) && retryNum < maxRetry;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }


    private boolean needRetry(Response response) {

        return !isSuccessful() && (response != null && !response.isSuccessful());
    }


}
