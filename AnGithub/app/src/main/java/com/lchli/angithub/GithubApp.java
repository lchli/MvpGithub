package com.lchli.angithub;

import android.app.Application;

import com.lchli.angithub.common.utils.ContextProvider;

/**
 * Created by lchli on 2016/10/29.
 */

public class GithubApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.initContext(this);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }
}
