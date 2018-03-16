package com.iqg.jimij.qgsms.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.iqg.jimij.qgsms.App
import com.iqg.jimij.qgsms.Const
import com.iqg.jimij.qgsms.R
import com.iqg.jimij.qgsms.model.beans.Contacter
import com.mobile.utils.permission.Permission
import com.mobile.utils.showToast
import com.mobile.utils.value
import kotlinx.android.synthetic.main.dialog_send_sms.view.*

/**
 * Created by jimiji on 2017/11/30.
 */
class SendsmsDialog() : DialogFragment() {
    lateinit var contacters: MutableList<Contacter>

    @SuppressLint("ValidFragment")
    constructor(contacters: MutableList<Contacter>) : this() {
        this.contacters = contacters
    }

    lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_send_sms, container, false)
        rootView.btnSend2.setOnClickListener {
            var message = rootView.smsMessage.value
            if (message.isNotEmpty()) {
                contacters.forEach {
                    //确保获得权限
                    SmsManager.getDefault().sendTextMessage(it.phone, null, message.replace("{name}", it.name).replace("{sex}", if (it.sex == Const.BOY) "师弟" else "师妹"), null, null)
                }
                showToast("发送完成")
                dismiss()
            }
        }
        return rootView
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        super.onResume()
    }
}