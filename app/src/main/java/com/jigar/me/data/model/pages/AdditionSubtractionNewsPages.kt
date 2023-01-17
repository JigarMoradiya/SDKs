package com.jigar.me.data.model.pages

data class AdditionSubtractionCategory(var category_id: String? = null,var category_name: String? = null,var pages: ArrayList<AdditionSubtractionPage> = arrayListOf())
data class AdditionSubtractionPage(var page_id: String? = null,var PageName: String? = null,var SortDesc: String? = null,var total_abacus: String? = null)
