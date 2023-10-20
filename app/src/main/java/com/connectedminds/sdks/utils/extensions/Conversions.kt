package com.connectedminds.sdks.utils.extensions

import android.content.res.Resources

// dp to pixels
val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Int.sp: Float get() =  ( this.dp / Resources.getSystem().displayMetrics.scaledDensity)
