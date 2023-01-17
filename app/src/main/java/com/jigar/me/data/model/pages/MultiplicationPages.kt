package com.jigar.me.data.model.pages

data class MultiplicationCategory(var category_name: String? = null,var pages: List<MultiplicationPages> = arrayListOf())
data class MultiplicationPages(val PageName: String, val que2_str: String,val que2_type: String, val que1_digit_type: Int,val id : String)
