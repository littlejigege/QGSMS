package com.iqg.jimij.qgsms;

import android.support.annotation.CallSuper;

/**
 * Created by jimiji on 2017/11/29.
 */

public abstract class BasePresenter<T> {
    protected T mView;

    public void takeView(T view) {
        mView = view;
    }


   public void dropView() {
        mView = null;
    }
}
