package com.lchli.angithub;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.shell.MainReactPackage;
import com.lchli.angithub.common.appEnv.AppEnvironmentFactory;
import com.lchli.angithub.common.utils.ContextProvider;
import com.lchli.angithub.common.utils.LogExt;
import com.lchli.angithub.common.utils.UniversalLog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lchli on 2016/10/29.
 */

public class GithubApp extends Application implements ReactApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    ContextProvider.initContext(this);
    UniversalLog.LOG_FLAG= AppEnvironmentFactory.ins().logFlag();
    UniversalLog.get().setLogExt(new LogImpl());
    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

    registerReactInstanceEventListener();
    //

  }

  private static class LogImpl implements LogExt {
    @Override
    public void e(String tag, String msg) {
      LogUtils.e(tag,msg);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
      LogUtils.e(tag,msg);
    }

    @Override
    public void e(String msg) {
      LogUtils.e(msg);
    }

  }

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    protected boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
              new MainReactPackage()
      );
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  private void registerReactInstanceEventListener() {
    mReactNativeHost.getReactInstanceManager().addReactInstanceEventListener(mReactInstanceEventListener);
  }
  private void unRegisterReactInstanceEventListener() {
    mReactNativeHost.getReactInstanceManager().removeReactInstanceEventListener(mReactInstanceEventListener);
  }
  private final ReactInstanceManager.ReactInstanceEventListener mReactInstanceEventListener = new ReactInstanceManager.ReactInstanceEventListener() {
    @Override
    public void onReactContextInitialized(ReactContext context) {
      mReactContext = context;
    }
  };

  private ReactContext mReactContext;

  public ReactContext getReactContext() {
    return mReactContext;
  }
}
