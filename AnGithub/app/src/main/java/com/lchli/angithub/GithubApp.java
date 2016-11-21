package com.lchli.angithub;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.lchli.angithub.common.constants.LocalConst;
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
    UniversalLog.LOG_FLAG=LocalConst.LOG_FLAG;
    UniversalLog.get().setLogExt(new LogImpl());
    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

  }

  private static class LogImpl implements LogExt {
    @Override
    public void e(String tag, String msg) {
      LogUtils.e(msg);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
      LogUtils.e(msg);
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
}
