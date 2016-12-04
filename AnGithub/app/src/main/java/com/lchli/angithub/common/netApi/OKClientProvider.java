package com.lchli.angithub.common.netApi;


import com.lchli.angithub.common.netApi.interceptor.CommonParamsInterceptor;
import com.lchli.angithub.common.netApi.interceptor.LogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by lchli on 2016/10/10.
 */

public final class OKClientProvider {

  private static final int CONNECT_TIME_OUT = 10;
  private static final int READ_TIME_OUT = 30;
  private static final int WRITE_TIME_OUT = 30;



  public static OkHttpClient.Builder getHttpClientBuilder() {


    return new OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(new CommonParamsInterceptor())
        .addInterceptor(new LogInterceptor());
  }

  // public static OkHttpClient.Builder getSSLClientBuilder() {
  // InputStream inputStream;
  // try {
  // inputStream = ContextProvider.context().getAssets().open("baidu.crt");
  // } catch (IOException e) {
  // throw new RuntimeException(e);
  // }
  // HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
  //
  // return getHttpClientBuilder().sslSocketFactory(sslParams.sSLSocketFactory,
  // sslParams.trustManager);
  // }


}
