package com.lchli.angithub.common.netApi.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lchli on 2016/10/31.
 */

public class CommonParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("osName", "Android")
                .addQueryParameter("osVersion", android.os.Build.VERSION.SDK_INT + "")
                .build();

        Request request = original.newBuilder()
                .header("User-Agent", "AnGithub")
                .header("Accept", "application/json")
                .method(original.method(), original.body())
                .url(url)
                .build();

        return chain.proceed(request);
    }

}
