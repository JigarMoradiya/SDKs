package com.jigar.me.data.local.data

import android.graphics.Bitmap

data class HomeBanner(val type: String, val image: Int? = null, val bitmap: Bitmap? = null)
data class ImagesDataObjects(val type: DataObjectsType, val name: String, val image: String)
data class BeginnerExamPaper(val type: BeginnerExamQuestionType, val value: String,val value2: String, val imageData: ImagesDataObjects, var userAnswer : String? = "")