package com.jigar.me.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.data.local.data.BeginnerExamPaper
import com.jigar.me.data.model.dbtable.exam.DailyExamData
import java.util.*

class DataTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun examDetailToList(data: String): List<DailyExamData> {
        val listType = object : TypeToken<List<DailyExamData>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToExamDetail(someObjects: List<DailyExamData>): String {
        return gson.toJson(someObjects)
    }
    @TypeConverter
    fun beginnerExamDetailToList(data: String): List<BeginnerExamPaper> {
        val listType = object : TypeToken<List<BeginnerExamPaper>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToBeginnerExamDetail(someObjects: List<BeginnerExamPaper>): String {
        return gson.toJson(someObjects)
    }

}