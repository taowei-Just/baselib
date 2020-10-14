package com.tao.mvpbaselibrary.lib_http.retrofit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


/**
 * 界面描述：
 * <p>
 */

public interface RequestHandler {

    Request handleRequest(Request request, Interceptor.Chain chain);

    Response handleResponse(Response response, Interceptor.Chain chain) throws IOException;

}
