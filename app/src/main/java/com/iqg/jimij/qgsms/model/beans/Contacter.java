package com.iqg.jimij.qgsms.model.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.iqg.jimij.qgsms.Const;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jimiji on 2017/11/29.
 */
@Entity
public class Contacter implements Comparable<Contacter> {
    @PrimaryKey
    @NonNull
    public String name;
    public String phone;
    public int sex;
    @Ignore
    public boolean isSelected;


    public char getPinying() {
        String[] pinying = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
        char var;
        try {
            switch (pinying[0]) {
                case "ceng2":
                    var = pinying[1].charAt(0);
                    break;
                default:
                    var = pinying[0].charAt(0);
            }
        } catch (Exception e) {
            var = '#';
        }
        return var;
    }

    public Contacter(@NonNull String name, String phone, int sex) {
        this.name = name;
        this.phone = phone;
        this.sex = sex;
    }

    @Override
    public int compareTo(@NonNull Contacter o) {

        return Const.INSTANCE.getCHINA_COMPARE().compare(getPinying() + "", o.getPinying() + "");
    }
}
