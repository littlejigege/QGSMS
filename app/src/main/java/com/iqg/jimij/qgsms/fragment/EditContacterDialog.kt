package com.iqg.jimij.qgsms.fragment

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.iqg.jimij.qgsms.App
import com.iqg.jimij.qgsms.BasePresenter
import com.iqg.jimij.qgsms.R
import com.iqg.jimij.qgsms.mainpage.MainPresenter

import com.iqg.jimij.qgsms.model.beans.Contacter
import com.mobile.utils.value
import kotlinx.android.synthetic.main.dialog_edit_contacter.view.*
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/11/30.
 */
class EditContacterDialog(ctx: Context, presenter: MainPresenter) {
    var dialog: AlertDialog.Builder
    private val rootView: View by lazy { LayoutInflater.from(ctx).inflate(R.layout.dialog_edit_contacter, null, false) }

    init {
        dialog = AlertDialog.Builder(ctx)
                .setView(rootView)
                .setTitle("添加联系人")
                .setPositiveButton("确定", { dialog, _ ->
                    dialog.dismiss()
                    thread {
                        addCOntacterToDB(Contacter(rootView.nameInputLayout.editText!!.value, rootView.phoneInputLayout.editText!!.value))
                        presenter.getContacterFromDB()
                    }

                })
                .setNegativeButton("取消", { dialog, _ ->
                    dialog.dismiss()
                })
    }

    fun show() {
        dialog.show()
    }

    private fun addCOntacterToDB(contacter: Contacter) {
        App.db.contacterDao().insertContacter(contacter)
    }

}