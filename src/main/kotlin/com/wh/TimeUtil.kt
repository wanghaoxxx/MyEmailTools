package com.wh

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    fun getMonth(mPageOffset: Int): String {
        val nowCalender = Calendar.getInstance()
        nowCalender.time = Date()
        nowCalender.add(Calendar.MONTH, mPageOffset)
        val df = SimpleDateFormat("yyyyMM", Locale.getDefault())
        return df.format(nowCalender.time)
    }

}