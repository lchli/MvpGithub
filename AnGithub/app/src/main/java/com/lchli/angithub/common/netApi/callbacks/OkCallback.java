package com.lchli.angithub.common.netApi.callbacks;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.lchli.angithub.Navigator;
import com.lchli.angithub.common.base.BaseGithubResponse;
import com.lchli.angithub.common.utils.ContextProvider;
import com.lchli.angithub.common.utils.UniversalLog;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class OkCallback<T> implements Callback {

  public static boolean DEBUG = true;

  private boolean isCallbackInUiThread = true;
  private String originResponseBody;
  private static final Handler handler = new Handler(Looper.getMainLooper());

  public OkCallback(boolean isCallbackInUiThread) {
    this.isCallbackInUiThread = isCallbackInUiThread;
  }

  public OkCallback() {
    this(true);
  }

  @Override
  public final void onFailure(Call call, IOException e) {
    fail(e);
  }

  @Override
  public final void onResponse(Call call, Response response) {
    if (response == null) {
      fail(new Exception("response is null."));
      return;
    }
    ResponseBody body = response.body();
    if (body == null) {
      fail(new Exception("responseBody is null."));
      return;
    }
    try {
      originResponseBody = body.string();
      final T t = new Gson().fromJson(originResponseBody, getSuperclassTypeParameter(getClass()));
      if (t instanceof BaseGithubResponse) {
        BaseGithubResponse github = (BaseGithubResponse) t;
        if (github.isHaveError()) {
          Navigator.toLogin(ContextProvider.context());
          return;
        }
      }
      success(t);

    } catch (Exception e) {
      if (DEBUG) {
        UniversalLog.get().e(e);
      }
      fail(e);
    }

  }


  private void fail(final Throwable e) {

    if (isCallbackInUiThread) {
      runInUiThread(new Runnable() {
        @Override
        public void run() {
          onFail(e);
        }
      });
    } else {
      onFail(e);
    }

  }

  private void success(final T t) {

    if (isCallbackInUiThread) {
      runInUiThread(new Runnable() {
        @Override
        public void run() {
          onSuccess(t);
        }
      });
    } else {
      onSuccess(t);
    }
  }


  private Type getSuperclassTypeParameter(Class<?> subclass) {
    Type superclass = subclass.getGenericSuperclass();
    if (superclass instanceof Class) {
      throw new RuntimeException("Missing type parameter.");
    }
    ParameterizedType parameter = (ParameterizedType) superclass;
    return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
  }


  public String getOkResponseBody() {
    return originResponseBody;
  }


  public abstract void onSuccess(T response);

  public abstract void onFail(Throwable error);

  public boolean isCallbackInUiThread() {
    return isCallbackInUiThread;
  }

  public void runInUiThread(Runnable r) {
    handler.post(r);
  }
}
