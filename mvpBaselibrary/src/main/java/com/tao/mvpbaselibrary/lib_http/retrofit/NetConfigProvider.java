package com.tao.mvpbaselibrary.lib_http.retrofit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * 界面描述：回调处理
 * <p>
 */

public interface NetConfigProvider {


    Interceptor[] configInterceptors();

    void configHttps(OkHttpClient.Builder builder);

    CallAdapter.Factory configCallFactory();

    Converter.Factory configConvertFactory();

    CookieJar configCookie();

    RequestHandler configHandler();

    String configBaseUrl();

    long configConnectTimeoutMills();

    long configReadTimeoutMills();

    long configWriteTimeoutMills();

    boolean configLogEnable();

    int configMaxRetry();


}
