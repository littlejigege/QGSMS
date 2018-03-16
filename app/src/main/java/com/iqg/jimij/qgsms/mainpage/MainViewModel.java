package com.iqg.jimij.qgsms.mainpage;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.iqg.jimij.qgsms.App;
import com.iqg.jimij.qgsms.model.ContacterReprtory;
import com.iqg.jimij.qgsms.model.beans.Contacter;
import com.mobile.utils.ToastUtilsKt;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 * Created by jimiji on 2017/11/29.
 */

public class MainViewModel extends ViewModel {
    private ContacterReprtory reprtory = new ContacterReprtory(App.db);
    private MutableLiveData<List<Contacter>> contactersServer;

    public LiveData<List<Contacter>> getContacterFromDB() {
        return reprtory.getAllContacterFromDb();
    }

    private void fetchServer(final int group) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactersServer.postValue(reprtory.getByGroupFromServer(group));
            }
        }).start();

    }

    public LiveData<List<Contacter>> getByGroup(int group) {
        if (contactersServer == null) {
            contactersServer = new MutableLiveData<>();

        }
        fetchServer(group);
        return contactersServer;
    }
}
