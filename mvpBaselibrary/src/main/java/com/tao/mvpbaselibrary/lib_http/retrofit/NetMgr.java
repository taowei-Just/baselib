package com.tao.mvpbaselibrary.lib_http.retrofit;

 

import com.tao.logger.log.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * 界面描述：Retrofit实现类
 * <p>
 */

public class NetMgr {
    private static final String TAG = "NetMgr";
    private final static int CONNECT_TIMEOUT_MILLS = 10 * 1000;
    private final static int READ_TIMEOUT_MILLS = 10 * 1000;
    private final static int WRITE_TIMEOUT_MILLS = 10 * 1000;
    /**
     * 默认的provider
     */
    private NetConfigProvider sProvider = null;
    private static NetMgr instance;
    private Map<String, NetConfigProvider> providerMap = new HashMap<>();
    private Map<String, Retrofit> retrofitMap = new HashMap<>();
    private Map<String, OkHttpClient> clientMap = new HashMap<>();

    public static final String UPLOAD_DEFAULT_KEY = "upload";


    public static NetMgr getInstance() {
        if (instance == null) {
            synchronized (NetMgr.class) {
                if (instance == null) {
                    instance = new NetMgr();
                }
            }
        }
        return instance;
    }


    public <S> S get(String baseUrl, Class<S> service) {
        return getInstance().getRetrofit(baseUrl).create(service);
    }

    /**
     * 传入默认的provider
     */
    public void registerProvider(NetConfigProvider provider) {
        this.sProvider = provider;
    }

    /**
     * 这个方法需要在设置过默认的provider后才可以使用
     * @param extraKey key
     * @param provider provider
     */

    public void registerProviderByDefaultBaseUrl(String extraKey, NetConfigProvider provider) {
        getInstance().providerMap.put(getMapKey(provider.configBaseUrl(), extraKey), provider);
    }


    public void registerProvider(String baseUrl, String extraKey, NetConfigProvider provider) {
        getInstance().providerMap.put(getMapKey(baseUrl, extraKey), provider);
    }


    public NetConfigProvider getCommonProvider() {
        return sProvider;
    }

    public void clearCache() {
        getInstance().retrofitMap.clear();
        getInstance().clientMap.clear();
    }

    public Retrofit getRetrofit(String baseUrl) {
        return getRetrofit(baseUrl, "", null);
    }


    /**
     * 获得retrofit 根据默认的baseUrl
     */
    public Retrofit getDefaultRetrofit() {
        if (this.sProvider != null) {
            return getRetrofit(this.sProvider.configBaseUrl(), "", null);
        }
        throw new IllegalStateException("Use Default Method,Provider can not be null");

    }

    public Retrofit getDefaultRetrofit(String extraKey) {
        if (this.sProvider != null) {
            return getRetrofit(this.sProvider.configBaseUrl(), extraKey, providerMap.get(getMapKey(this.sProvider.configBaseUrl(), extraKey)));
        }
        throw new IllegalStateException("BaseUrl is From defaultProvider,Please set Default Provider");

    }


    public Retrofit getRetrofit(String baseUrl, String extraKey) {
        if (baseUrl == null) {
            if (sProvider != null) {
                baseUrl = sProvider.configBaseUrl();
            }
        }
        return getRetrofit(baseUrl, extraKey, null);
    }

    private Retrofit getRetrofit(String baseUrl, String extraKey, NetConfigProvider provider) {
        if (empty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }

        if (retrofitMap.get(getMapKey(baseUrl, extraKey)) != null) {
            return retrofitMap.get(getMapKey(baseUrl, extraKey));
        }

        if (provider == null) {
            provider = providerMap.get(getMapKey(baseUrl, extraKey));
            if (provider == null) {
                provider = sProvider;
            }
        }
        checkProvider(provider);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, extraKey, provider))
                .addCallAdapterFactory(provider.configCallFactory())
                .addConverterFactory(provider.configConvertFactory());

        Retrofit retrofit = builder.build();
        retrofitMap.put(getMapKey(baseUrl, extraKey), retrofit);
        providerMap.put(getMapKey(baseUrl, extraKey), provider);

        return retrofit;
    }

    private boolean empty(String baseUrl) {
        return baseUrl == null || baseUrl.isEmpty();
    }


    private OkHttpClient getClient(String baseUrl, String extra, NetConfigProvider provider) {

        if (empty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
        if (clientMap.get(getMapKey(baseUrl, extra)) != null) {
            return clientMap.get(getMapKey(baseUrl, extra));
        }

        checkProvider(provider);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();


        builder.connectTimeout(provider.configConnectTimeoutMills() != 0
                ? provider.configConnectTimeoutMills()
                : CONNECT_TIMEOUT_MILLS, TimeUnit.MILLISECONDS);
        builder.readTimeout(provider.configReadTimeoutMills() != 0
                ? provider.configReadTimeoutMills() : READ_TIMEOUT_MILLS, TimeUnit.MILLISECONDS);

        builder.writeTimeout(provider.configWriteTimeoutMills() != 0
                ? provider.configReadTimeoutMills() : WRITE_TIMEOUT_MILLS, TimeUnit.MILLISECONDS);
        CookieJar cookieJar = provider.configCookie();

        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        provider.configHttps(builder);
        builder.addInterceptor(new NetInterceptor(provider));

        Interceptor[] interceptors = provider.configInterceptors();
        if (!empty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        if (provider.configLogEnable()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel();
            builder.addInterceptor(Logger.getOkHttpInterceptor());
        }

        OkHttpClient client = builder.build();
        clientMap.put(getMapKey(baseUrl, extra), client);
        providerMap.put(getMapKey(baseUrl, extra), provider);

        return client;
    }

    private boolean empty(Interceptor[] interceptors) {
        return interceptors == null || interceptors.length == 0;
    }

    private void checkProvider(NetConfigProvider provider) {
        if (provider == null) {
            throw new IllegalStateException("must register provider first");
        }
    }


    /**
     * 线程切换
     *
     * @param observable o
     * @return Observable
     */

    private <T> Observable<T> handleThread(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Map<String, Retrofit> getRetrofitMap() {
        return retrofitMap;
    }

    public Map<String, OkHttpClient> getClientMap() {
        return clientMap;
    }

    public Map<String, NetConfigProvider> getProviderMap() {
        return providerMap;
    }

    private String getMapKey(String url, String extraKey) {
        return url + extraKey;
    }

}
