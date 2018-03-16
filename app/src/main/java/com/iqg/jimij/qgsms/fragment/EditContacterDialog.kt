package com.iqg.jimij.qgsms.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.iqg.jimij.qgsms.App
import com.iqg.jimij.qgsms.Const
import com.iqg.jimij.qgsms.R
import com.iqg.jimij.qgsms.mainpage.MainViewModel

import com.iqg.jimij.qgsms.model.beans.Contacter
import com.mobile.utils.coroutine
import com.mobile.utils.showInputMethod
import com.mobile.utils.showToast
import com.mobile.utils.value
import kotlinx.android.synthetic.main.dialog_edit_contacter.view.*
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/11/30.
 */
class EditContacterDialog(ctx: Context, model: MainViewModel) {
    var dialog: AlertDialog
    private val rootView: View by lazy { LayoutInflater.from(ctx).inflate(R.layout.dialog_edit_contacter, null, false) }

    init {
        rootView.radioGroup.check(R.id.rbtnGirl)
        dialog = AlertDialog.Builder(ctx)
                .setView(rootView)
                .setTitle("添加联系人")
                .setPositiveButton("确定", { _, _ ->
                })
                .setNegativeButton("取消", { dialog, _ ->
                    dialog.dismiss()
                }).create()

        //为了信息不完整时不取消dialog，这里重新改一下监听器
        //在onshow中改监听器是因为没显示之前，getbutton为空
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                if (rootView.nameInputLayout.editText!!.value.isNotEmpty() and rootView.nameInputLayout.editText!!.value.isNotEmpty()) {
                    //防止重复
                    coroutine({ App.db.contacterDao().getByName(rootView.nameInputLayout.editText!!.value) }) {
                        if (it != null) {
                            showToast("此人已存在")
                        } else {
                            dialog.dismiss()
                            thread {
                                addCOntacterToDB(Contacter(rootView.nameInputLayout.editText!!.value,
                                        rootView.phoneInputLayout.editText!!.value,
                                        getSex()))
                            }
                        }
                    }
                } else {
                    showToast("填写完整...")
                }
            }
        }

    }

    fun show() {
        dialog.show()
    }

    private fun addCOntacterToDB(contacter: Contacter) {
        App.db.contacterDao().insertContacter(contacter)
    }

    private fun getSex() = when (rootView.radioGroup.checkedRadioButtonId) {
        R.id.rbtnBoy -> {
            Const.BOY
        }
        else -> {
            Const.GIRL
        }
    }


}