package com.jigar.me.data.model.pages

data class SingleDigitCategory(var category_name: String? = null,var pages: List<SingleDigitPages> = arrayListOf())
data class SingleDigitPages(val id : String, val from: Int, val to: Int, val type_random: Boolean,var PageName: String? = null)
