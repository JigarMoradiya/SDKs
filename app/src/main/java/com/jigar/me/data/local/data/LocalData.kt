package com.jigar.me.data.local.data

import android.graphics.Bitmap

data class ColorData(val color: Int, val darkColor: Int)
data class HomeBanner(val type: String, val image: Int? = null, val bitmap: Bitmap? = null)
data class ImagesDataObjects(val type: DataObjectsType, val name: String, val image: String)
data class BeginnerExamPaper(val type: BeginnerExamQuestionType, val value: String,val value2: String, val imageData: ImagesDataObjects, var userAnswer : String? = "")

data class ExerciseLevel(val id : String, val title: String, val list : ArrayList<ExerciseLevelDetail>)
data class ExerciseLevelDetail(val totalQue : Int,val queLines : Int,val digits : Int, val totalTime: Int)
data class ExerciseList(val question : String, val answer : Int, var userAnswer : String? = null)