package com.connectedminds.sdks.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View

val View.res: Resources get() = resources
val View.ctx: Context get() = context

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }
fun View.toggleVis() { if (visibility==View.VISIBLE){ visibility = View.GONE } else{ visibility = View.VISIBLE } }

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) {
    setOnClickListener { func() }
}

inline fun <T : View> T.onLongClick(crossinline func: T.() -> Unit) {
    setOnLongClickListener { func(); true }
}


