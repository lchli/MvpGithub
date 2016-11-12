package com.lchli.angithub.common.netApi;

import com.lchli.angithub.common.constants.Urlconst;
import com.lchli.angithub.common.utils.Preconditions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lchli on 2016/10/29.
 */

public class RestClient {

  private HostSelectionInterceptor hostSelectionInterceptor;
  private Map<String, Object> servicesCache = new HashMap<>();
  private Retrofit retrofit;

  private static class RestClientHolder {
    private static final RestClient CLIENT = new RestClient();
  }

  private RestClient() {
    hostSelectionInterceptor = new HostSelectionInterceptor();
    OkHttpClient okHttpClient = OKClientProvider.getHttpClientBuilder()
        .addInterceptor(hostSelectionInterceptor)
        .build();
    retrofit = new Retrofit.Builder()
        .baseUrl(HttpUrl.parse(Urlconst.Github.HOST))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .callFactory(okHttpClient)
        .build();
  }

  public static final RestClient instance() {
    return RestClientHolder.CLIENT;
  }


  public <T> T createService(Class<T> serviceClass, String baseUrl) {
    Preconditions.checkNotNull(serviceClass, "serviceClass cannot be null.");
    if (baseUrl != null) {
      hostSelectionInterceptor.setHost(baseUrl);
    }

    String serviceName = serviceClass.getName();
    Object service = servicesCache.get(serviceName);
    if (service != null) {
      return (T) service;
    }
    service = retrofit.create(serviceClass);
    servicesCache.put(serviceName, service);
    return (T) service;
  }

  public <T> T createService(Class<T> serviceClass) {
    return createService(serviceClass, null);
  }


  private static final class HostSelectionInterceptor implements Interceptor {

    private volatile String host;

    public void setHost(String host) {
      this.host = host;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      String host = this.host;
      if (host != null) {
        HttpUrl newUrl = request.url().newBuilder()
            .host(host)
            .build();
        request = request.newBuilder()
            .url(newUrl)
            .build();
      }
      return chain.proceed(request);
    }
  }
}
