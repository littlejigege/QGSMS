package com.iqg.jimij.qgsms.mainpage;

import android.app.Activity;

import com.iqg.jimij.qgsms.App;
import com.iqg.jimij.qgsms.BasePresenter;
import com.iqg.jimij.qgsms.model.beans.Contacter;
import com.mobile.utils.ToastUtilsKt;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 * Created by jimiji on 2017/11/29.
 */

public class MainPresenter extends BasePresenter<View> {

    public void getContacterFromDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Contacter> list = App.db.contacterDao().loadAllContacters();
                ((Activity) mView).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.showListView(list);
                    }
                });
            }
        }).start();
    }
}
