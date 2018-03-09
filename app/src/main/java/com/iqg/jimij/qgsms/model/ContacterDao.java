package com.iqg.jimij.qgsms.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.iqg.jimij.qgsms.model.beans.Contacter;

import java.util.List;

/**
 * Created by jimiji on 2017/11/29.
 */
@Dao
public interface ContacterDao {
    @Query("SELECT * FROM contacter")
    public List<Contacter> loadAllContacters();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertContacter(Contacter... contacters);

    @Delete
    public void deleteContacter(Contacter... contacters);
}
