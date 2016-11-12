package com.lchli.angithub;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.lchli.angithub.common.constants.LocalConst;
import com.lchli.angithub.common.utils.ContextProvider;

/**
 * Created by lchli on 2016/10/29.
 */

public class GithubApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.initContext(this);
        LogUtils.getLogConfig().configAllowLog(LocalConst.LOG_FLAG);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

    }
}
