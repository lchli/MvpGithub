package com.lchli.angithub.common.base;

import java.lang.ref.WeakReference;

/**
 * Created by lichenghang on 16/12/3.
 */

public abstract class BaseLoader {

    protected <T> WeakReference<T> weakRefCallback(T callback){
        return new WeakReference<>(callback);
    }
}
