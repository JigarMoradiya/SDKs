package com.example.iiifa_fan_android.data.models

import com.google.gson.annotations.SerializedName


class ForceUpdate(
    var by_pass: Int,
    var force_update: Int,
    var app_link: String,
    var reason: String,
    var title: String,
    @SerializedName("show_update_popup_epoch")
    var _show_update_popup_epoch: Long
) {
    val show_update_popup_epoch: Long
        get() = _show_update_popup_epoch * 1000
}