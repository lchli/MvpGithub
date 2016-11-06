package com.lchli.angithub.features.me.controller;

import android.util.Base64;

import com.apkfuns.logutils.LogUtils;
import com.lchli.angithub.common.constants.LocalConst;
import com.lchli.angithub.common.netApi.RestClient;
import com.lchli.angithub.common.netApi.apiService.GithubRepository;
import com.lchli.angithub.common.utils.ExceptionLoger;
import com.lchli.angithub.common.utils.Preconditions;
import com.lchli.angithub.features.me.bean.AuthPostParam;
import com.lchli.angithub.features.me.bean.AuthResponse;
import com.lchli.angithub.features.me.bean.CurrentUserInfoResponse;
import com.lchli.angithub.features.me.model.UserAccountRepository;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lchli on 2016/11/6.
 */

public class UserController {

  private final CompositeSubscription mCompositeSubscription;

  public interface Callback {
    void onFail(String msg);

    void onSuccess(CurrentUserInfoResponse data);
  }

  private Callback mCallback;

  public UserController(Callback mCallback) {
    this.mCallback = Preconditions.checkNotNull(mCallback);
    mCompositeSubscription = new CompositeSubscription();
  }

  public void login(String userName, String password) {

    String userCredentials = userName + ":" + password;
    String basicAuth =
        "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));

    final AuthPostParam authPostParam = new AuthPostParam();
    authPostParam.note = LocalConst.Github.NOTE;
    authPostParam.client_id = LocalConst.Github.CLIENT_ID;
    authPostParam.client_secret = LocalConst.Github.CLIENT_SECRET;
    authPostParam.scopes = LocalConst.Github.SCOPES;

    final GithubRepository repo = RestClient.instance().createService(GithubRepository.class);
    unsubscripe();
    Subscription subscription = repo.authorize(authPostParam, basicAuth.trim())
        .flatMap(new Func1<AuthResponse, Observable<CurrentUserInfoResponse>>() {
          @Override
          public Observable<CurrentUserInfoResponse> call(AuthResponse authResponse) {
            if (authResponse == null || authResponse.token == null) {
              return null;
            }
            LogUtils.e("token:" + authResponse.token);
            UserAccountRepository.get().saveAccount(authResponse);
            return repo.getCurrentUserInfo(authResponse.token);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<CurrentUserInfoResponse>() {
          @Override
          public void onCompleted() {

      }

          @Override
          public void onError(Throwable e) {
            ExceptionLoger.logException(e);
            mCallback.onFail(e.getMessage());
          }

          @Override
          public void onNext(CurrentUserInfoResponse data) {
            mCallback.onSuccess(data);
          }
        });
    mCompositeSubscription.add(subscription);
  }

  public void loadUserInfo() {
    unsubscripe();
    Subscription subscription = Observable.create(new Observable.OnSubscribe<AuthResponse>() {
      @Override
      public void call(Subscriber<? super AuthResponse> subscriber) {
        subscriber.onNext(UserAccountRepository.get().getAccount());
      }
    }).flatMap(new Func1<AuthResponse, Observable<CurrentUserInfoResponse>>() {
      @Override
      public Observable<CurrentUserInfoResponse> call(AuthResponse authResponse) {
        if (authResponse == null) {
          return null;
        }
        return RestClient.instance().createService(GithubRepository.class)
            .getCurrentUserInfo(authResponse.token);
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<CurrentUserInfoResponse>() {
          @Override
          public void onCompleted() {

      }

          @Override
          public void onError(Throwable e) {
            ExceptionLoger.logException(e);
            mCallback.onFail(e.getMessage());

          }

          @Override
          public void onNext(CurrentUserInfoResponse userInfoResponse) {
            mCallback.onSuccess(userInfoResponse);
          }
        });

    mCompositeSubscription.add(subscription);
  }

  public void unsubscripe() {
    mCompositeSubscription.clear();
  }
}
