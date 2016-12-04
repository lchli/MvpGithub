package com.lchli.angithub.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.lchli.angithub.GithubApp;

public abstract class BaseReactFragment extends BaseFragment {

  private ReactRootView mReactRootView;
  private ReactInstanceManager mReactInstanceManager;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mReactRootView = new ReactRootView(activity);
    mReactInstanceManager = ((GithubApp) getActivity().getApplication()).getReactNativeHost()
        .getReactInstanceManager();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return mReactRootView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {}

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mReactRootView.startReactApplication(mReactInstanceManager, getMainPageName(), new Bundle());
  }

  protected abstract String getMainPageName();

  protected void sendEvent(String eventName,
      @Nullable WritableMap params) {
    if (((GithubApp) getActivity().getApplication()).getReactContext() != null) {
      ((GithubApp) getActivity().getApplication()).getReactContext()
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
          .emit(eventName, params);
    }
  }
}
