package com.lchli.angithub.common.netApi.interceptor;

import com.lchli.angithub.common.utils.UniversalLog;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 打印服务器返回的内容。
 * Created by lchli on 16-11-10.
 */
public class LogInterceptor implements Interceptor {

  private static final Charset UTF8 = Charset.forName("UTF-8");

  private static final String TAG = LogInterceptor.class.getSimpleName();
  public static boolean DEBUG = true;

  @Override
  public Response intercept(Chain chain) throws IOException {

    log("intercept>>>>>>>>>>>>>>>>>>>>>>>>>>");

    Request request = chain.request();
    Response response;
    try {
      response = chain.proceed(request);
    } catch (IOException e) {
      logException(e);
      throw e;
    }
    try {
      ResponseBody responseBody = response.body();

      BufferedSource source = responseBody.source();
      source.request(Long.MAX_VALUE); // Buffer the entire body.
      Buffer buffer = source.buffer();

      Charset charset = UTF8;
      MediaType contentType = responseBody.contentType();
      if (contentType != null) {
        try {
          charset = contentType.charset(UTF8);
        } catch (UnsupportedCharsetException e) {
          logException(e);
          return response;
        }
      }

      String origin = buffer.clone().readString(charset);

      String logMsg = String.format(Locale.ENGLISH, "okResponse original string[%s]:\n%s",
          request.url().url().toString(), origin);
      log(logMsg);

    } catch (Exception e) {
      logException(e);
    }
    return response;
  }

  private static void logException(Exception e) {

    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    sw.flush();
    String log = sw.toString();
    try {
      sw.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    log(log);

  }

  private static void log(String log) {
    if (DEBUG) {
      UniversalLog.get().e(TAG, log);
    }
  }
}
