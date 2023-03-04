package com.jigar.me.data.model.pages

import com.google.gson.annotations.SerializedName

data class AdditionSubtractionCategory(
    @SerializedName("level_id")
    var level_id: String? = null,
    @SerializedName("category_id")
    var category_id: String? = null,
    @SerializedName("category_name")
    var category_name: String? = null,
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
    @SerializedName("total_abacus")
    var total_abacus: String? = null
)
