package com.iqg.jimij.qgsms.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iqg.jimij.qgsms.Const
import com.iqg.jimij.qgsms.model.beans.Contacter
import com.mobile.utils.showToast
import com.mobile.utils.toast
import java.net.URL

/**
 * Created by jimiji on 2018/3/11.
 */
class ContacterReprtory(val database: ContacterDatabase) {
    fun getAllContacterFromDb(): LiveData<List<Contacter>> = database.contacterDao().loadAllContacters()
    fun getByGroupFromServer(group: Int): MutableList<Contacter>? {
        var text = ""
        try {
            text = URL("${Const.serverAdress}$group").readText()
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.toast()
            return null
        }
        println(text)
        var liveData: MutableList<Contacter> = mutableListOf()
        if (text != "[]") {
            try {
                liveData = Gson().fromJson(text, object : TypeToken<MutableList<Contacter>>() {}.type)
            } catch (e: Exception) {
                e.printStackTrace()
                e.message?.toast()
                return null
            }
        }
        return liveData
    }

}