package com.lchli.angithub.common.constants;

import android.os.Environment;

import com.lchli.angithub.common.appEnv.AppEnvironmentFactory;

/**
 * Created by lchli on 2016/11/1.
 */

public final class LocalConst {

  public interface Github {
    String CLIENT_ID = "d0e675d71c8de66020f0";
    String CLIENT_SECRET = "d9f3bb3356df8b723946a25674b986a32348c29d";
    String[] SCOPES = {"user", "repo", "notifications", "gist", "admin:org"};
    String NOTE = "AnGithub";
  }

  public static final boolean LOG_FLAG = AppEnvironmentFactory.getEnv().logFlag();
  public static final String SD_PATH= Environment.getExternalStorageDirectory().getAbsolutePath();
}
