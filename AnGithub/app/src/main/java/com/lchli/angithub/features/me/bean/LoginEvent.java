package com.lchli.angithub.features.me.bean;

/**
 * Created by lchli on 2016/11/2.
 */

public class LoginEvent {

  public CurrentUserInfoResponse authInfo;

  public LoginEvent(CurrentUserInfoResponse authInfo) {
    this.authInfo = authInfo;
  }
}
