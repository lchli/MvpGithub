package com.lchli.angithub.common.utils;

public interface LogExt {
    void e(String tag, String msg);

    void e(String tag, String msg, Throwable tr);

    void e(String msg);

  }