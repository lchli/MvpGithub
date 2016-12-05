package com.lchli.angithub;


import com.apkfuns.logutils.LogUtils;
import com.lchli.angithub.common.constants.LocalConst;
import com.lchli.angithub.common.utils.UniversalLog;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by lchli on 2016/8/14.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

  // private.private static final String EXCEPTION_DIR = String.format("%s/%s",
  // LocalConst.STUDY_APP_ROOT_DIR, "Exception");
  // private.private static final String RECENT_EXCEPTION_FILE = String.format("%s/%s",
  // EXCEPTION_DIR, "RecentException.txt");

  static {
    // FileUtils.mkdirs(new File(EXCEPTION_DIR));
    // ExtFileUtils.makeFile(RECENT_EXCEPTION_FILE);
  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    logException(ex);
  }

  public static void logException(Throwable ex) {
    UniversalLog.get().e(ex);

    try {
      FileUtils.write(new File(LocalConst.SD_PATH, "githubException.txt"),
          UniversalLog.getExceptionStacktrace(ex));
    } catch (IOException e) {
      e.printStackTrace();
      LogUtils.e("app exception log save fail.");
    }
  }
}
