package com.tao.mvpbaselibrary.lib_http.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author chengang
 * @date 2019-06-05
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.basic.net.retrofit
 * @describe
 */
public abstract class AbstractDefaultNetProvider implements NetConfigProvider {

    private static final long CONNECT_TIME_OUT = 10*1000;
    private static final long READ_TIME_OUT = 180*1000;
    private static final long WRITE_TIME_OUT = 10*1000;
    private boolean DEBUG =false;

    public AbstractDefaultNetProvider() {
    }

    public AbstractDefaultNetProvider(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    @Override
    public Interceptor[] configInterceptors() {
        return null;
    }

    @Override
    public void configHttps(OkHttpClient.Builder builder) {

    }

    @Override
    public CallAdapter.Factory configCallFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Override
    public Converter.Factory configConvertFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")

                .create();
        return GsonConverterFactory.create(gson);
    }

    @Override
    public CookieJar configCookie() {
        return null;
    }



    @Override
    public long configConnectTimeoutMills() {
        return CONNECT_TIME_OUT;
    }

    @Override
    public long configReadTimeoutMills() {
        return READ_TIME_OUT;
    }

    @Override
    public long configWriteTimeoutMills() {
        return WRITE_TIME_OUT;
    }

    @Override
    public boolean configLogEnable() {
        return DEBUG;
    }

    @Override
    public int configMaxRetry() {
        return 3;
    }


}
