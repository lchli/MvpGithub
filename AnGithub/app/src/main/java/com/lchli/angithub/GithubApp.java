package com.lchli.angithub;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.lchli.angithub.common.constants.LocalConst;
import com.lchli.angithub.common.utils.ContextProvider;
import com.lchli.angithub.common.utils.LogExt;
import com.lchli.angithub.common.utils.UniversalLog;

/**
 * Created by lchli on 2016/10/29.
 */

public class GithubApp extends Application {

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
}
