package com.jigar.me.data.model

import com.google.gson.annotations.SerializedName

data class PojoAbacus(
    @SerializedName("abacus_id") var abacusId: String = "",
    @SerializedName("type") var type: String = "",
    @SerializedName("page_id") var pageId: String = "",
    @SerializedName("Que0") var que0: String = "",
    @SerializedName("Que1") var que1: String = "",
    @SerializedName("Que2") var que2: String = "",
    @SerializedName("Que3") var que3: String = "",
    @SerializedName("Que4") var que4: String = "",
    @SerializedName("Que5") var que5: String = "",
    @SerializedName("Ans") var ans: String = "",
    @SerializedName("Hint1") var hint1: String = "",
    @SerializedName("Hint2") var hint2: String = "",
    @SerializedName("Hint3") var hint3: String = "",
    @SerializedName("Hint4") var hint4: String = "",
    @SerializedName("Hint5") var hint5: String = "",
    @SerializedName("Sign1") var sign1: String = "",
    @SerializedName("Sign2") var sign2: String = "",
    @SerializedName("Sign3") var sign3: String = "",
    @SerializedName("Sign4") var sign4: String = "",
    @SerializedName("Sign5") var sign5: String = "")