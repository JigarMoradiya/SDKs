package com.jigar.me.utils

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    var ddMMMyyyyhhmma: String = "dd MMM yyyy hh:mm a"
    var yyyy_MM_dd_HH_mm: String = "yyyy_MM_dd_hh_mm"

    fun convertDateFormat(
        date: String,
        sourceStr: String,
        destinationStr: String
    ): String {
        var strNewDate = date
        val newDate: Date
        val source = SimpleDateFormat(sourceStr, Locale.getDefault())
        val destination = SimpleDateFormat(destinationStr, Locale.getDefault())
        try {
            if (!TextUtils.isEmpty(date)) {
                newDate = source.parse(date)
                strNewDate = destination.format(newDate)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return strNewDate
    }

    fun getDateString(date: Date,format : String): String {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        return sdf.format(date)
    }
    fun isTimeAfter(startTime: Date?, endTime: Date): Boolean {
        return !endTime.before(startTime)
    }
}