package com.iqg.jimij.qgsms

import android.content.ContentResolver
import android.widget.Toast
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by jimiji on 2018/3/21.
 */
internal class SmsObserver(handler: Handler,val cr:ContentResolver)// TODO Auto-generated constructor stub
    : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) {
        println("===================onChange=============")
        // TODO Auto-generated method stub
        //查询发送向箱中的短信
        val cursor = cr.query(Uri.parse(
                "content://sms/sent"), null, null, null, null)
        //遍历查询结果获取用户正在发送的短信
        while (cursor.moveToNext()) {
            val sb = StringBuffer()
            //获取短信的发送地址
            sb.append("发送地址：" + cursor.getString(cursor.getColumnIndex("address")))
            //获取短信的标题
            sb.append("\n标题：" + cursor.getString(cursor.getColumnIndex("subject")))
            //获取短信的内容
            sb.append("\n内容：" + cursor.getString(cursor.getColumnIndex("body")))
            //获取短信的发送时间
            val date = Date(cursor.getLong(cursor.getColumnIndex("date")))
            //格式化以秒为单位的日期
            val sdf = SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒")
            sb.append("\n时间：" + sdf.format(date))
            println("查询到的正在发送的短信：" + sb.toString())
        }
        super.onChange(selfChange)
    }

}