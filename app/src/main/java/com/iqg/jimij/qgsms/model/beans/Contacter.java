package com.iqg.jimij.qgsms.model.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by jimiji on 2017/11/29.
 */
@Entity
public class Contacter {
    @PrimaryKey
    @NonNull
    public String name;
    public String phone;
    @Ignore
    public boolean isSelected;

    public Contacter(@NonNull String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
