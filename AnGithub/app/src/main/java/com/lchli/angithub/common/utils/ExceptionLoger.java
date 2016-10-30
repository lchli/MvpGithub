package com.lchli.angithub.common.utils;

import com.apkfuns.logutils.LogUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by lchli on 2016/10/29.
 */

public class ExceptionLoger {

    public static void logException(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        printWriter.close();
        LogUtils.e(stringWriter.toString());
    }
}
