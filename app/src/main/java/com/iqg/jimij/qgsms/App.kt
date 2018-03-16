package com.iqg.jimij.qgsms

import android.app.Application
import android.arch.persistence.room.Room
import com.iqg.jimij.qgsms.model.ContacterDatabase
import com.mobile.utils.Utils

/**
 * Created by jimiji on 2017/11/29.
 */
class App : Application() {
    companion object {
        lateinit var db: ContacterDatabase
    }

    override fun onCreate() {
        super.onCreate()
        setupRoom()
        Utils.init(this)
    }

    private fun setupRoom() {
        db = Room.databaseBuilder(this, ContacterDatabase::class.java, "database").apply {
            //Room迁移自动删库
            fallbackToDestructiveMigration()
        }.build()
    }
}