package com.example.iiifa_fan_android.utils.extensions

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import android.widget.SeekBar


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

fun View.enableDisable(isEnable : Boolean) {
    isEnabled = isEnable
    isClickable = isEnable
}

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) {
    setOnClickListener { func() }
}

inline fun <T : View> T.onLongClick(crossinline func: T.() -> Unit) {
    setOnLongClickListener { func(); true }
}

@SuppressLint("ClickableViewAccessibility")
fun SeekBar.setProgress(lastProgress : Int, progress : Int,isTouchDisable : Boolean = true){
    val anim = ValueAnimator.ofInt(lastProgress, progress)
    anim.duration = 1000
    anim.addUpdateListener { animation ->
        val animProgress = animation.animatedValue as Int
        this.progress = animProgress
    }
    anim.start()

    /*
    * seekbar touch disable
    * */
    this.setOnTouchListener { v, event -> isTouchDisable }
}
fun ProgressBar.setProgress(lastProgress : Int, progress : Int){
    val anim = ValueAnimator.ofInt(lastProgress, progress)
    anim.duration = 1000
    anim.addUpdateListener { animation ->
        val animProgress = animation.animatedValue as Int
        this.progress = animProgress
    }
    anim.start()
}

