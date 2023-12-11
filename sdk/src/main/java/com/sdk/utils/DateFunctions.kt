package com.sdk.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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
    @RequiresApi(Build.VERSION_CODES.O) // 26 api
    fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate? {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    fun covertTimeAgoForPodcast(date: Long): String? {
        var convTime: String? = null
        var suffix = ""
        suffix = "ago"

        try {
            val dateDiff = System.currentTimeMillis() - date
            var second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            var minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            var hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            var day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)


            second = abs(second)
            minute = abs(minute)
            hour = abs(hour)
            day = abs(day)

            if (second < 5) {
                convTime = "Just now"
            }else if (second < 60) {
                convTime = "$second seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute minutes $suffix"
            } else if (hour < 24) {
                convTime = if (hour == 1L){
                    "$hour an hour $suffix"
                }else{
                    "$hour hours $suffix"
                }

            }else if(day < 7){
                if (day == 1L){
                    convTime = "$day day $suffix"
                }else{
                    convTime = "$day days $suffix"
                }

            } else  { // if (day >= 7)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = date
                    val startLocalDate = convertToLocalDateViaInstant(cal.time)
                    val endLocalDate = convertToLocalDateViaInstant(Date())
                    val diff  = Period.between(startLocalDate,endLocalDate)
                    val year = diff.years
                    val month = diff.months
                    val days = diff.days
                    convTime = if (year  == 1){
                        "A year $suffix"
                    }else if (year > 1){
                        "$year years $suffix"
                    }else if (month  == 1){
                        "A month $suffix"
                    }else if (month > 1){
                        "$month months $suffix"
                    }else if ((days / 7).toString() == "1") {
                        "A week $suffix"
                    }else {
                        (days / 7).toString() + " weeks $suffix"
                    }
                }else{
                    convTime = if (day > 360) {
                        if ((day / 360).toString() == "1")
                            "A year $suffix"
                        else
                            (day / 360).toString() + " years $suffix"
                    } else if (day > 30) {
                        if ((day / 30).toString() == "1")
                            "A month $suffix"
                        else
                            (day / 30).toString() + " months $suffix"
                    } else {
                        if ((day / 7).toString() == "1")
                            "A week $suffix"
                        else
                            (day / 7).toString() + " weeks $suffix"
                    }
                }

            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }

}