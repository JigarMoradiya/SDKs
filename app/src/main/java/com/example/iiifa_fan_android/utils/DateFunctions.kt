package com.example.iiifa_fan_android.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.TimeUnit

//Date parsing from long to string and string to long according to give pattern
class DateFunctions {
    private val milliseconds: Long = 0

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun convertDateInMilliSeconds(date: String?, pattern: String?): Long {

            //SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss aa yyyy", Locale.ENGLISH);
            val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
            sdf.timeZone = TimeZone.getDefault()
            try {
                val mDate = sdf.parse(date)
                return mDate.time
            } catch (e: Exception) {
                Log.e("ErrorInParsing", e.localizedMessage)
            }
            return null!!.toLong()
        }

        fun convertMilliSecondsToDate(finalResult: Long?, pattern: String?): String {
            val date = Date(finalResult!!)
            val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
            formatter.timeZone = TimeZone.getDefault()
            return formatter.format(date)
        }

        fun convertMilliSecondsToDateUTC(finalResult: Long?, pattern: String?): String {
            val date = Date(finalResult!!)
            val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(date)
        }

        fun dateFromUTC(date: Date): Date {
            return Date(date.time + Calendar.getInstance().timeZone.getOffset(date.time))
        }

        fun getAge(date: String?): Int {
            var age = 0
            try {
                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val date1 = dateFormat.parse(date)
                val now = Calendar.getInstance()
                val dob = Calendar.getInstance()
                dob.time = date1
                require(!dob.after(now)) { "Can't be born in the future" }
                val year1 = now[Calendar.YEAR]
                val year2 = dob[Calendar.YEAR]
                age = year1 - year2
                val month1 = now[Calendar.MONTH]
                val month2 = dob[Calendar.MONTH]
                if (month2 > month1) {
                    age--
                } else if (month1 == month2) {
                    val day1 = now[Calendar.DAY_OF_MONTH]
                    val day2 = dob[Calendar.DAY_OF_MONTH]
                    if (day2 > day1) {
                        age--
                    }
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return age
        }

        fun getAlphaNumericString(n: Int): String {

            // chose a Character random from this String
            val AlphaNumericString = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz")

            // create StringBuffer size of AlphaNumericString
            val sb = StringBuilder(n)
            for (i in 0 until n) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                val index = (AlphaNumericString.length
                        * Math.random()).toInt()

                // add Character one by one in end of sb
                sb.append(AlphaNumericString[index])
            }
            return sb.toString()
        }

        fun getTimeAgo(date: Long, today_pattern: String?, pattern: String?): String {
            var time = date
            if (time < 1000000000000L) {
                time *= 1000
            }
            val now = currentDate().time
            if (time > now || time <= 0) {
                return "Now"
            }
            val diff = now - time
            return if (diff < 24 * HOUR_MILLIS) {
                val date_new = Date(date)
                val formatter = SimpleDateFormat(today_pattern, Locale.ENGLISH)
                formatter.timeZone = TimeZone.getDefault()
                formatter.format(date)
            } else if (diff < 48 * HOUR_MILLIS) {
                "Yesterday"
            } else {
                val date_new = Date(date)
                val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
                formatter.timeZone = TimeZone.getDefault()
                formatter.format(date)
            }
        }


        fun getTimeAgoForPost(date: Long): String? {
            var time = date
            if (time < 1000000000000L) {
                time *= 1000
            }
            val now: Long = currentDate().time
            if (time <= 0) {
                return "now"
            }
            if (time > now) {
                return convertMilliSecondsToDate(time, "EEE, d MMM yyyy")
            }
            val diff = now - time
            return if (diff < MINUTE_MILLIS) {
                "moments ago"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "a minute ago"
            } else if (diff < 60 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " minutes ago"
            } else if (diff < 2 * HOUR_MILLIS) {
                "an hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " hours ago"
            } else {
                convertMilliSecondsToDate(time, "EEE, d MMM yyyy")
            }
        }


        fun covertTimeAgoForPodcast(date: Long): String? {
            var convTime: String? = null
            val prefix = ""

            var suffix = ""
            suffix = "Ago"

            try {
                val dateDiff = System.currentTimeMillis() - date
                var second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
                var minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
                var hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
                var day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)

                second = Math.abs(second)
                minute = Math.abs(minute)
                hour = Math.abs(hour)
                day = Math.abs(day)

                if (second < 60) {
                    convTime = "$second seconds $suffix"
                } else if (minute < 60) {
                    convTime = "$minute minutes $suffix"
                } else if (hour < 24) {
                    convTime = "$hour hours $suffix"
                } else if (day >= 7) {
                    convTime = if (day > 360) {
                        if ((day / 360).toString() == "1")
                            (day / 360).toString() + " year $suffix"
                        else
                            (day / 360).toString() + " years $suffix"
                    } else if (day > 30) {
                        if ((day / 30).toString() == "1")
                            (day / 30).toString() + " month $suffix"
                        else
                            (day / 30).toString() + " months $suffix"
                    } else {
                        if ((day / 7).toString() == "1")
                            (day / 7).toString() + " week $suffix"
                        else
                            (day / 7).toString() + " weeks $suffix"
                    }
                } else if (day < 7) {
                    convTime = "$day days $suffix"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                Log.e("ConvTimeE", e.message!!)
            }
            return convTime
        }


        fun covertTimeAgoForPodcast(date: Long, showSuffix: Boolean = true): String? {
            var convTime: String? = null


            try {
                var second: Long = TimeUnit.MINUTES.toSeconds(date)
                var minute: Long = TimeUnit.MINUTES.toMinutes(date)
                var hour: Long = TimeUnit.MINUTES.toHours(date)
                var day: Long = TimeUnit.MINUTES.toDays(date)

                second = Math.abs(second)
                minute = Math.abs(minute)
                hour = Math.abs(hour)
                day = Math.abs(day)

                if (second <= 60) {
                    convTime = "$second Seconds "
                } else if (minute <= 60) {
                    convTime = "$minute Minutes"
                } else if (hour <= 24) {
                    convTime = "$hour Hours"
                } else if (day >= 7) {
                    convTime = if (day > 360) {
                        if ((day / 360).toString() == "1")
                            (day / 360).toString() + " Year "
                        else
                            (day / 360).toString() + " Years "
                    } else if (day > 30) {
                        if ((day / 30).toString() == "1")
                            (day / 30).toString() + " Month "
                        else
                            (day / 30).toString() + " Months "
                    } else {
                        if ((day / 7).toString() == "1")
                            (day / 7).toString() + " Week "
                        else
                            (day / 7).toString() + " Weeks "
                    }
                } else if (day < 7) {
                    convTime = "$day Days"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                Log.e("ConvTimeE", e.message!!)
            }
            return convTime
        }


        //get current Date
        private fun currentDate(): Date {
            val calendar = Calendar.getInstance()
            return calendar.time
        }


        @JvmStatic
        fun getTimeString(millis: Long): String? {
            val buf = StringBuffer()
            val hours = (millis / (1000 * 60 * 60)).toInt()
            val minutes = (millis % (1000 * 60 * 60) / (1000 * 60)).toInt()
            val seconds = (millis % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
            buf
                    .append(String.format("%02d", hours))
                    .append(":")
                    .append(String.format("%02d", minutes))
                    .append(":")
                    .append(String.format("%02d", seconds))
            return buf.toString()
        }


        @JvmStatic
        fun dislayAudioDuration(value: Long): String? {
            val audioTime: String
            val t = value.toInt()
//            val hrs = dur / 3600000
//            val mns = dur / 60000 % 60000
//            val scs = dur % 60000 / 1000
//            audioTime = if (hrs > 0) {
//                String.format("%02d:%02d:%02d", hrs, mns, scs)
//            } else {
//                String.format("%02d:%02d", mns, scs)
//            }

            val hour: Int = t / 60 //since both are ints, you get an int

            val mi: Int = t % 60

            if (hour > 0) {
                return "$hour hr $mi mins"
            } else {
                return "$mi mins"
            }


        }


        @JvmStatic
        fun getEndTimeOfTheDay(instance: Calendar): Long? {
            instance.set(instance.get(Calendar.YEAR),
                    instance.get(Calendar.MONTH),
                    instance.get(Calendar.DAY_OF_MONTH),
                    23,
                    59,
                    59)

            return TimeUnit.MILLISECONDS.toSeconds(instance.timeInMillis)
        }

        @JvmStatic
        fun getStartTimeOfTheDay(instance: Calendar): Long? {
            instance.set(instance.get(Calendar.YEAR),
                    instance.get(Calendar.MONTH),
                    instance.get(Calendar.DAY_OF_MONTH),
                    0,
                    0,
                    0)

            return TimeUnit.MILLISECONDS.toSeconds(instance.timeInMillis)
        }


        @JvmStatic
        fun getChatTimeAgo(date: Long, today_pattern: String?, pattern: String?): String? {
            var time = date
            if (time < 1000000000000L) {
                time *= 1000
            }
            val now = currentDate().time
            if (time > now || time <= 0) {
                return "Now"
            }
            val diff = now - time
            return if (diff < 24 * HOUR_MILLIS) {
                val date_new = Date(date)
                val formatter = SimpleDateFormat(today_pattern, Locale.ENGLISH)
                formatter.timeZone = TimeZone.getDefault()
                formatter.format(date)
            } else if (diff < 48 * HOUR_MILLIS) {
                "Yesterday"
            } else {
                val date_new = Date(date)
                val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
                formatter.timeZone = TimeZone.getDefault()
                formatter.format(date)
            }
        }


        @JvmStatic
        fun getDateFormat(format: String?, time: Long?): String? {
            return try {
                val df: DateFormat = SimpleDateFormat(format, Locale.ENGLISH)
                df.timeZone = TimeZone.getDefault()
                df.format(time)
            } catch (e: java.lang.Exception) {
                Log.e("date exception", e.toString())
                ""
            }
        }




        @JvmStatic
        fun getDifferenceBetweenTwoTimeZone(): Int {
            val timezone = TimeZone.getDefault()
            val millis = timezone.getRawOffset();
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
            Log.e("Time zone  Minute Diff", minutes.toString())
            val remainder = Math.abs(minutes % 60)

            Log.e("Time zone  remainder", remainder.toString())

            if (remainder != 0L) {
                Log.e("Time zone", remainder.toString())
            }

            return remainder.toInt()
        }

        @JvmStatic
        fun getMorningStartSlots(timeInMillies: Long): Long {
            //        6 AM - 12 PM Morning slots
//        12 PM - 5 PM Afternoon slots
//        5 PM - 9 PM Evening Slots
//        9 PM - 6 AM Night Slots
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 6, 0] =
                    0
            return cal.timeInMillis
        }

        @JvmStatic
        fun getMorningEndSlots(timeInMillies: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 11, 59] =
                    59
            return cal.timeInMillis
        }

        @JvmStatic
        fun getNoonStartSlots(timeInMillies: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 12, 0] =
                    0
            return cal.timeInMillis
        }

        @JvmStatic
        fun getNoonEndSlots(timeInMillies: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 16, 59] =
                    59
            return cal.timeInMillis
        }

        @JvmStatic
        fun getEveningStartSlots(timeInMillies: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 17, 0] =
                    0
            Log.e("eve_start", cal.timeInMillis.toString())
            return cal.timeInMillis
        }

        @JvmStatic
        fun getEveningEndSlots(timeInMillies: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 20, 59] =
                    59
            Log.e("eve_night", cal.timeInMillis.toString())
            return cal.timeInMillis
        }

        @JvmStatic
        fun getNightStartSlots(timeInMillies: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 21, 0] =
                    0
            Log.e("night_start", cal.timeInMillis.toString())
            return cal.timeInMillis
        }

        @JvmStatic
        fun getNightEndSlots(timeInMillies: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = timeInMillies
            cal[cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE], 5, 59] =
                    59
            Log.e("night_end", cal.timeInMillis.toString())
            return cal.timeInMillis
        }


        @JvmStatic
        fun getAlldays(): Array<String> {
            val days = arrayOf(
                    "Sunday",
                    "Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday"
            )
            return days
        }

        fun getMonthNames(year: Long): List<Long> {
            val months = ArrayList<Long>()

            val passedYear = DateFunctions.convertMilliSecondsToDate(year, "yyyy")
            val cal = Calendar.getInstance()
            val currentYear = cal.get(Calendar.YEAR)
            val currentMonth = cal.get(Calendar.MONTH)


            Log.e("years", passedYear + " " + currentMonth + " " + currentYear)

            if (passedYear == "2022") {
                for (i in 2..currentMonth) {
                    val cal = Calendar.getInstance()
                    cal[Calendar.MONTH] = i
                    months.add(cal.timeInMillis)
                }
            } else if (passedYear == currentYear.toString()) {
                for (i in 0..currentMonth) {
                    val cal = Calendar.getInstance()
                    cal[Calendar.MONTH] = i
                    months.add(cal.timeInMillis)
                }
            } else {
                for (i in 0..11) {
                    val cal = Calendar.getInstance()
                    cal[Calendar.MONTH] = i
                    months.add(cal.timeInMillis)
                }
            }

            return months
        }


        fun getTotalYears(): List<Long> {
            val years = ArrayList<Long>()
            val cal = Calendar.getInstance()

            for (i in 2022..cal.get(Calendar.YEAR)) {
                val cal = Calendar.getInstance()
                cal[Calendar.YEAR] = i
                years.add(cal.timeInMillis)
            }

            return years
        }


        @JvmStatic
        fun getDayIndex(day: String): Int {
            when (day) {
                "Sunday" -> {
                    return 0
                }

                "Monday" -> {
                    return 1
                }

                "Tuesday" -> {
                    return 2
                }

                "Wednesday" -> {
                    return 3
                }

                "Thursday" -> {
                    return 4
                }

                "Friday" -> {
                    return 5
                }

                "Saturday" -> {
                    return 6
                }

                else -> {
                    return 0
                }
            }
        }



        @JvmStatic
        fun getEmojiIndex(day: Int): String {
            when (day) {
                1 -> {
                    return "☺️"
                }

                2 -> {
                    return "😇"
                }

                3 -> {
                    return "😖"
                }

                4 -> {
                    return "😞"
                }

                5 -> {
                    return "🙄"
                }

                else -> {
                    return ""
                }
            }
        }
    }


}