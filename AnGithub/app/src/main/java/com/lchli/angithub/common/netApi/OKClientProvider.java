package com.lchli.angithub.common.netApi;


import com.lchli.angithub.common.netApi.interceptor.CommonParamsInterceptor;
import com.lchli.angithub.common.utils.ContextProvider;
import com.lchli.angithub.common.utils.UniversalLog;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lchli on 2016/10/10.
 */

public final class OKClientProvider {

  private static final int CONNECT_TIME_OUT = 10;
  private static final int READ_TIME_OUT = 30;
  private static final int WRITE_TIME_OUT = 30;



  public static OkHttpClient.Builder getHttpClientBuilder() {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new OkhttpLoger());
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    return new OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(new CommonParamsInterceptor())
        .addInterceptor(logging);
  }

  public static OkHttpClient.Builder getSSLClientBuilder() {
    InputStream inputStream;
    try {
      inputStream = ContextProvider.context().getAssets().open("baidu.crt");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

    return getHttpClientBuilder().sslSocketFactory(sslParams.sSLSocketFactory,
        sslParams.trustManager);
  }

  private static class OkhttpLoger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
      UniversalLog.get().e(message);
    }
  }
}
