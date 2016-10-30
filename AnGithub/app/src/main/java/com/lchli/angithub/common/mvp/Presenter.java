package com.lchli.angithub.common.mvp;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}