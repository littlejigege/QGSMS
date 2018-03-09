package com.iqg.jimij.qgsms.model;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.iqg.jimij.qgsms.model.beans.Contacter;

/**
 * Created by jimiji on 2017/11/29.
 */
@Database(entities = {Contacter.class}, version = 1,exportSchema = false)
public abstract class ContacterDatabase extends RoomDatabase {
    public abstract ContacterDao contacterDao();
}
