package com.lchli.angithub.common.appEnv;


import com.lchli.angithub.BuildConfig;

/**
 * Created by lchli on 2016/10/26.
 */

public class AppEnvironmentFactory implements AppEnvironment {

  private AppEnvironment currentEnv = initEnv();


  private AppEnvironment initEnv() {
    switch (BuildConfig.BUILD_TYPE) {
      case "release":
        return new ReleaseEnv();
      case "debug":
        return new DebugEnv();
      default:
        return new DebugEnv();

    }
  }

  private static AppEnvironmentFactory INS = new AppEnvironmentFactory();

  public static AppEnvironmentFactory ins() {
    return INS;
  }


  @Override
  public String getIP() {
    return currentEnv.getIP();
  }

  @Override
  public boolean logFlag() {
    return currentEnv.logFlag();
  }
}
