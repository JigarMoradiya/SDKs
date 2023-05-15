package com.jigar.me.data.model.pages

import com.google.gson.annotations.SerializedName

data class AdditionSubtractionCategory(
    @SerializedName("level_id")
    var level_id: String? = null,
    @SerializedName("category_id")
    var category_id: String? = null,
    @SerializedName("category_name")
    var category_name: String? = null,
    @SerializedName("category_name_ar")
    var category_name_ar: String? = null,
    @SerializedName("pages")
    var pages: ArrayList<AdditionSubtractionPage> = arrayListOf()
)

data class AdditionSubtractionPage(
    @SerializedName("page_id")
    var page_id: String? = null,
    @SerializedName("PageName")
    var PageName: String? = null,
    @SerializedName("SortDesc")
    var SortDesc: String? = null,

    @SerializedName("PageName_ar")
    var PageName_ar: String? = null,
    @SerializedName("SortDesc_ar")
    var SortDesc_ar: String? = null,

    @SerializedName("hint")
    var hint: String? = null,
    @SerializedName("file")
    var file: String? = null
)
