package com.lchli.angithub.common.netApi;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lchli on 2016/4/25.
 */
public class FileClient {

  static {
    OkHttpUtils.initClient(OKClientProvider.getHttpClientBuilder().build());
  }


  public static RequestCall downloadFile(String url, FileCallBack callback) {
    RequestCall call = OkHttpUtils
        .get()
        .url(url)
        .build();
    call.execute(callback);
    return call;
  }

  public static RequestCall uploadFile(final String url, Map<String, String> textParams,
      Map<String, File> fileParams, Callback callback) {

    PostFormBuilder builder = OkHttpUtils.post().url(url);
    if (!isNullOrEmpty(fileParams)) {
      Set<Map.Entry<String, File>> entrySet = fileParams.entrySet();
      for (Map.Entry<String, File> entry : entrySet) {
        builder.addFile(entry.getKey(), entry.getValue().getName(), entry.getValue());
      }
    }

    RequestCall call = builder.params(textParams).build();
    call.execute(callback);
    return call;
  }

  public static RequestCall uploadFile(final String url, Map<String, String> textParams,
      List<File> fileParams, String filesParamName, Callback callback) {

    PostFormBuilder builder = OkHttpUtils.post().url(url);
    if (!isEmptyList(fileParams)) {
      for (File entry : fileParams) {
        builder.addFile(filesParamName, entry.getName(), entry);
      }
    }
    RequestCall call = builder.params(textParams).build();
    call.execute(callback);
    return call;
  }



  private static boolean isNullOrEmpty(Map map) {
    return map == null || map.isEmpty();
  }


  private static boolean isEmptyList(List list) {
    return list == null || list.isEmpty();
  }


}
