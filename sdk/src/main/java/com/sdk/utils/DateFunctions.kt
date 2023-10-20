package com.sdk.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFunctions {
    const val MMM_dd_yyyy = "MMM dd, YYYY"
    fun convertMilliSecondsToDate(finalResult: Long?, pattern: String?): String {
        val date = Date(finalResult!!)
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(date)
    }
    fun displayAudioDuration(value: Long): String {
        val t = value.toInt()
        val hour: Int = t / 60 //since both are ints, you get an int
        val mi: Int = t % 60
        return if (hour > 0) {
            "$hour hr $mi mins"
        } else {
            "$mi mins"
        }
    }
}