package com.example.iiifa_fan_android.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

val View.res: Resources get() = resources
val View.ctx: Context get() = context

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }
fun View.toggleVis() {
    visibility = if (visibility==View.VISIBLE){
        View.GONE
    } else{
        View.VISIBLE
    }
}

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) {
    setOnClickListener { func() }
}

inline fun <T : View> T.onLongClick(crossinline func: T.() -> Unit) {
    setOnLongClickListener { func(); true }
}

