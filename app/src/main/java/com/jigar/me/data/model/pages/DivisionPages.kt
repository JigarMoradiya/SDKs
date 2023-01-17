package com.jigar.me.data.model.pages

data class DivisionCategory(var category_name: String? = null,var pages: List<DivisionPages> = arrayListOf())
data class DivisionPages(val PageName: String, val que2_str: String,val que2_type: String, val id : String)
