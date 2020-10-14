package com.tao.mvpbaselibrary.retrofitrx;


import com.tao.mvpbaselibrary.config.HttpConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {


    private String baseUrl;
    private Retrofit mBaseRetrofit;
    private Retrofit mGsonRetrofit;
    private Retrofit mRxRetrofit;
    private Retrofit mRxGsonRetrofit;
    private static Map<String, RetrofitFactory> factoryMap = new HashMap<>();
    private final OkHttpClient mOkHttpClient;

    private RetrofitFactory(String baseUrl, Interceptor interceptor) {
        OkHttpClient.Builder build = new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)
//                .addInterceptor(InterceptorUtil.tokenInterceptor())
//                .addInterceptor(new HttpLoggingInterceptor() )//添加日志拦截器
//                .addNetworkInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        
//                        return null;
//                    }
//                })
                ;
        if (interceptor !=null)
            build.addInterceptor(interceptor);
        mOkHttpClient = build.build();
        this.baseUrl = baseUrl;
    }

    public OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 快速获取一个实例  用于对实例创建比较熟悉时
     *
     * @return
     * @throws Exception
     */
    public static synchronized RetrofitFactory getInstence() throws Exception {
        if (factoryMap.size() > 0)
            return factoryMap.get(0);
        throw new Exception("none factory Instance please use getInstence(\"baseUrl\")");
    }

    /**
     * @param s baseUrl
     * @return
     * @throws Exception 创建实例时传参错误
     */

    public static synchronized RetrofitFactory getInstence(String s) throws Exception {
        if (null == s || s.length() == 0)
            throw new Exception(" error  baseUrl must be not null");
        if (!factoryMap.containsKey(s)) {
            synchronized (RetrofitFactory.class) {
                factoryMap.put(s, new RetrofitFactory(s,null));
            }
        }
        return factoryMap.get(s);
    }

    /**
     * 普通的retrofit factory
     */
    public synchronized <T> T baseAPI(Class<T> clazz) {
        if (null == mBaseRetrofit) {
            synchronized (RetrofitFactory.class) {
                mBaseRetrofit = new Retrofit.Builder().baseUrl(baseUrl).client(mOkHttpClient).build();
            }
        }
        return mBaseRetrofit.create(clazz);
    }

    /**
     * 带gson解析器retrofit factory
     */
    public synchronized <T> T gsonAPI(Class<T> clazz) {
        if (null == mGsonRetrofit) {
            synchronized (RetrofitFactory.class) {
                mGsonRetrofit = new Retrofit.Builder().baseUrl(baseUrl).client(mOkHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build();
            }
        }
        return mGsonRetrofit.create(clazz);
    }

    /**
     * 使用rxjava框架普通的retrofit factory
     */
    public synchronized <T> T rxAPI(Class<T> clazz) {
        if (null == mRxRetrofit) {
            synchronized (RetrofitFactory.class) {
                mRxRetrofit = new Retrofit.Builder().baseUrl(baseUrl).client(mOkHttpClient)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
            }
        }
        return mRxRetrofit.create(clazz);
    }

    /**
     * rxjava gson解析的retrofit factory
     *
     * @param clazz apiclass
     * @param <T>   返回传入的类型
     * @return
     */

    public synchronized <T> T rxGsonAPI(Class<T> clazz) {
        if (null == mRxGsonRetrofit) {
            synchronized (RetrofitFactory.class) {
                mRxGsonRetrofit = new Retrofit.Builder().baseUrl(baseUrl).client(mOkHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        }
        return mRxGsonRetrofit.create(clazz);
    }

    /**
     * 更新baseurl
     *
     * @param baseurl
     */

    public void updataBaseUrl(String baseurl) {
        this.baseUrl = baseurl;
        if (null != mBaseRetrofit) {
            mBaseRetrofit = null;
            try {
                baseAPI(null);
            } catch (Exception e) {

            }
        }
        if (null != mGsonRetrofit) {
            mGsonRetrofit = null;
            try {
                gsonAPI(null);
            } catch (Exception e) {

            }
        }
        if (null != mRxRetrofit) {
            mRxRetrofit = null;
            try {
                rxAPI(null);
            } catch (Exception e) {

            }
        }
        if (null != mRxGsonRetrofit) {
            mRxGsonRetrofit = null;
            try {
                rxGsonAPI(null);
            } catch (Exception e) {

            }
        }
    }


}
