package com.iqg.jimij.qgsms

import java.text.Collator
import java.util.*

/**
 * Created by jimiji on 2018/3/11.
 */
object Const {
    var serverAdress = "http://10.21.48.11:8080/sms?group="
    val BOY = 1
    val GIRL = 2
    val CHINA_COMPARE = Collator.getInstance(Locale.ENGLISH)
}