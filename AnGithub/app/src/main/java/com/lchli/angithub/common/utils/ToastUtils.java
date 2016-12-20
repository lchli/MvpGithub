package com.lchli.angithub.common.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Locale;

public class ToastUtils {

  private static Context context = ContextProvider.context();

  public static void systemToast(int id) {
    systemToast(ResUtils.parseString(id));
  }

  public static void systemToast(String format, Object... args) {
    systemToast(buildMsg(format, args));
  }

  public static void systemToast(final String msg) {
    if (TextUtils.isEmpty(msg)) {
      return;
    }
    if (Looper.myLooper() != Looper.getMainLooper()) {
      UiHandler.post(new Runnable() {
        @Override
        public void run() {
          Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
      });
    } else {
      Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
  }

  private static String buildMsg(String format, Object... args) {
    return (args == null) ? format : String.format(Locale.US, format,
        args);
  }

}
