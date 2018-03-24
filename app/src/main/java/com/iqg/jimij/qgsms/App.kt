package com.iqg.jimij.qgsms

import android.app.Application
import android.arch.persistence.room.Room
import com.iqg.jimij.qgsms.model.ContacterDatabase
import com.mobile.utils.Utils
import com.mobile.utils.showToast
import com.taobao.sophix.SophixManager
import com.taobao.sophix.PatchStatus
import com.taobao.sophix.listener.PatchLoadStatusListener


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
        setupHotFix()
        Utils.init(this)
    }

    private fun setupRoom() {
        db = Room.databaseBuilder(this, ContacterDatabase::class.java, "database").apply {
            //Room迁移自动删库
            fallbackToDestructiveMigration()
        }.build()
    }

    private fun setupHotFix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub { mode, code, info, handlePatchVersion ->
                    // 补丁加载回调通知
                    when (code) {
                        PatchStatus.CODE_LOAD_SUCCESS -> {
                            // 表明补丁加载成功
                            println("补丁加载成功")
                        }
                        PatchStatus.CODE_LOAD_RELAUNCH -> {
                            showToast("补丁安装完成，重启生效")
                        }
                        else -> {
                            println("补丁下载错误码：$code")
                        }
                    }
                }.initialize()

    }
}