package com.example.iiifa_fan_android.data.models

import com.google.gson.annotations.SerializedName

data class FAQModel(
    @SerializedName("question")
    var question: String? = null,

    @SerializedName("answer")
    var answer: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("status")
    var isStatus: Boolean = false,

    var name: String? = null,
    var faqs: List<FAQModel> = ArrayList(),
    var id: String? = null

)
