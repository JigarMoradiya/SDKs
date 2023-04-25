package com.jigar.me.data.local.data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class ColorData(val color: Int, val darkColor: Int)
data class HomeBanner(val type: String, val image: Int? = null, val bitmap: Bitmap? = null)
data class HomeMenu(val type: Int, val image: Int)
data class AbacusContent(val type: String, val beadImage: Int, val abacusFrame135 : Int, val abacusFrameExam135 : Int, val dividerColor1 : Int, val resetBtnColor8 : Int, val beadHeight : Int, val beadWidth : Int, val beadSpace : Int,
                         val topBeadClose : Int, val topBeadOpen : Int, var bottomBeadClose: ArrayList<Int>, var bottomBeadOpen: ArrayList<Int>)
data class OtherApps(val type: Int, val image: Int,val name : String, val url : String)
data class ImagesDataObjects(val type: DataObjectsType, val name: String, val image: String)
data class BeginnerExamPaper(val type: BeginnerExamQuestionType, val value: String,val value2: String, val imageData: ImagesDataObjects, var userAnswer : String? = "", var isAbacusQuestion : Boolean? = false)

data class ExerciseLevel(val id : String, val title: String, val list : ArrayList<ExerciseLevelDetail>,var selectedChildPos : Int = 0)
data class ExerciseLevelDetail(val id : String,val totalQue : Int,val queLines : Int,val digits : Int, val totalTime: Int)
data class ExerciseList(val question : String, val answer : Int, var userAnswer : Int = -1)