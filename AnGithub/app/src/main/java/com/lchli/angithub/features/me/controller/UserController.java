package com.lchli.angithub.features.me.controller;

import android.os.AsyncTask;
import android.util.Base64;

import com.lchli.angithub.common.base.BaseLoader;
import com.lchli.angithub.common.constants.LocalConst;
import com.lchli.angithub.common.netApi.RestClient;
import com.lchli.angithub.common.netApi.apiService.GithubRepository;
import com.lchli.angithub.common.netApi.callbacks.RetrofitCallback;
import com.lchli.angithub.common.utils.Preconditions;
import com.lchli.angithub.features.me.bean.AuthPostParam;
import com.lchli.angithub.features.me.bean.AuthResponse;
import com.lchli.angithub.features.me.bean.CurrentUserInfoResponse;
import com.lchli.angithub.features.me.model.UserAccountRepository;

import java.lang.ref.WeakReference;

import retrofit2.Call;

/**
 * Created by lchli on 2016/11/6.
 */

public class UserController extends BaseLoader {

  private Call<AuthResponse> authCall;
  private Call<CurrentUserInfoResponse> userInfoCall;

  public interface Callback {
    void onFail(String msg);

    void onSuccess(CurrentUserInfoResponse data);
  }



  public void login(final String userName, String password, Callback mCallback) {

    if (authCall != null) {
      authCall.cancel();
      authCall = null;
    }

    String userCredentials = userName + ":" + password;
    String basicAuth =
        "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));

    final AuthPostParam authPostParam = new AuthPostParam();
    authPostParam.note = LocalConst.Github.NOTE;
    authPostParam.client_id = LocalConst.Github.CLIENT_ID;
    authPostParam.client_secret = LocalConst.Github.CLIENT_SECRET;
    authPostParam.scopes = LocalConst.Github.SCOPES;

    Preconditions.checkNotNull(mCallback);
    final WeakReference<Callback> mCallbackRef = weakRefCallback(mCallback);

    final GithubRepository repo = RestClient.instance().createService(GithubRepository.class);
    authCall = repo.authorize(authPostParam, basicAuth.trim());
    authCall.enqueue(new RetrofitCallback<AuthResponse>() {
      @Override
      public void onSuccess(final AuthResponse response) {
        if (response.token == null) {
          Callback cb = mCallbackRef.get();
          if (cb != null) {
            cb.onFail("token is null.");
          }
          return;
        }
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            UserAccountRepository.get().saveAccount(response);
            return null;
          }

          @Override
          protected void onPostExecute(Void aVoid) {
            Callback cb = mCallbackRef.get();
            if (cb != null) {
              loadUserInfo(cb);
            }
          }
        }.execute();
      }

      @Override
      public void onFail(Throwable error) {
        Callback cb = mCallbackRef.get();
        if (cb != null) {
          cb.onFail(error.getMessage());
        }
      }
    });


  }

  public void loadUserInfo(Callback mCallback) {

    if (userInfoCall != null) {
      userInfoCall.cancel();
      userInfoCall = null;
    }

    Preconditions.checkNotNull(mCallback);
    final WeakReference<Callback> mCallbackRef = weakRefCallback(mCallback);

    new AsyncTask<Void, Void, AuthResponse>() {
      @Override
      protected AuthResponse doInBackground(Void... params) {
        return UserAccountRepository.get().getAccount();
      }

      @Override
      protected void onPostExecute(AuthResponse authResponse) {
        if (authResponse == null) {
          Callback cb = mCallbackRef.get();
          if (cb != null) {
            cb.onFail("authResponse is null.");
          }
          return;
        }
        userInfoCall = RestClient.instance().createService(GithubRepository.class)
            .getCurrentUserInfo(authResponse.token);
        userInfoCall.enqueue(new RetrofitCallback<CurrentUserInfoResponse>() {
          @Override
          public void onSuccess(final CurrentUserInfoResponse response) {
            new AsyncTask<Void, Void, Void>() {
              @Override
              protected Void doInBackground(Void... params) {
                UserAccountRepository.get().saveUserInfo(response);
                return null;
              }

              @Override
              protected void onPostExecute(Void aVoid) {
                Callback cb = mCallbackRef.get();
                if (cb != null) {
                  cb.onSuccess(response);
                }
              }
            }.execute();
          }

          @Override
          public void onFail(Throwable error) {
            Callback cb = mCallbackRef.get();
            if (cb != null) {
              cb.onFail(error.getMessage());
            }
          }
        });

      }
    }.execute();

  }

  public void unsubscripe() {

    if (userInfoCall != null) {
      userInfoCall.cancel();
      userInfoCall = null;
    }

    if (authCall != null) {
      authCall.cancel();
      authCall = null;
    }

  }
}
