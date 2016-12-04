package com.lchli.angithub.common.netApi.callbacks;

import com.lchli.angithub.Navigator;
import com.lchli.angithub.common.base.BaseGithubResponse;
import com.lchli.angithub.common.utils.ContextProvider;

public abstract class RetrofitCallback<T> implements retrofit2.Callback<T> {

  public static boolean DEBUG = true;


  public abstract void onSuccess(T response);

  public abstract void onFail(Throwable error);


  @Override
  public void onResponse(retrofit2.Call<T> call, retrofit2.Response<T> response) {
    if (response == null) {
      onFail(new Exception("response is null."));
      return;
    }

    T t = response.body();
    if (t == null) {
      onFail(new Exception("responseBody is null."));
      return;
    }
    if (t instanceof BaseGithubResponse) {
      BaseGithubResponse github = (BaseGithubResponse) t;
      if (github.isHaveError()) {
        Navigator.toLogin(ContextProvider.context());
        return;
      }
    }

    onSuccess(t);

  }

  @Override
  public void onFailure(retrofit2.Call<T> call, Throwable t) {
    onFail(t);
  }
}
