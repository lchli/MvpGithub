package com.lchli.angithub.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by lchli on 2016/11/13.
 */

public class UniversalLog implements LogExt {

  public static boolean LOG_FLAG = true;

  private static UniversalLog universalLog = new UniversalLog();

  public static UniversalLog get() {
    return universalLog;
  }

  private LogExt logExt = new LogExt() {
    @Override
    public void e(String tag, String msg) {

    }

    @Override
    public void e(String tag, String msg, Throwable tr) {

    }

    @Override
    public void e(String msg) {

    }


  };

  private static String getExceptionStacktrace(Throwable ex) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    ex.printStackTrace(printWriter);
    printWriter.flush();
    String log = stringWriter.toString();
    printWriter.close();
    return log;
  }

  public void setLogExt(LogExt log) {
    if (log == null) {
      throw new IllegalArgumentException("log cannot be null.");
    }
    logExt = log;
  }

  @Override
  public void e(String tag, String msg) {
    if (LOG_FLAG) {
      logExt.e(tag, msg);
    }
  }

  @Override
  public void e(String tag, String msg, Throwable tr) {
    if (LOG_FLAG) {
      logExt.e(tag, msg, tr);
    }
  }

  @Override
  public void e(String msg) {
    if (LOG_FLAG) {
      logExt.e(msg);
    }
  }

  public void e(Throwable tr) {
    if (LOG_FLAG) {
      logExt.e(getExceptionStacktrace(tr));
    }
  }
}
