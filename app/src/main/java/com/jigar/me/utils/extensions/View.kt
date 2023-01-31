package com.jigar.me.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.jigar.me.R
import com.jigar.me.utils.Constants

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

fun View.setBlinkAnimation(){
    val animation: Animation = AlphaAnimation(1F, Constants.EXAM_CLICK_ON_CORRECT_ANSWER_ANIMATION_ALPHA) //to change visibility from visible to invisible
    animation.duration = Constants.EXAM_CLICK_ON_CORRECT_ANSWER_ANIMATION_DURATION //duration for each animation cycle
    animation.interpolator = LinearInterpolator()
    animation.repeatCount = Animation.INFINITE //repeating indefinitely
    animation.repeatMode = Animation.REVERSE //animation will start from end point once ended.
    startAnimation(animation) //to start animation
}
fun View.setExamObjectShakeAnimation(){
    val animShake: Animation = AnimationUtils.loadAnimation(context, R.anim.shake_exam_objects)
    startAnimation(animShake)
}

