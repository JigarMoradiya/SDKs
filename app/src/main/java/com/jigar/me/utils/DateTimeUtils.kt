package com.jigar.me.utils

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object DateTimeUtils {
    var ddMMMyyyyhhmma: String = "dd MMM yyyy hh:mm a"
    var yyyy_MM_dd_HH_mm: String = "yyyy_MM_dd_hh_mm"
    fun displayDurationHourMinSec(totalSecs: Long): String? {
        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60
        val seconds = totalSecs % 60
        val outputStr = if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
        return outputStr
    }
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

    @RequiresApi(Build.VERSION_CODES.O) // 26 api
    fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate? {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}