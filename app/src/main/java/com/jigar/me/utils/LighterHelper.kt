package com.jigar.me.utils

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation

class LighterHelper {
    companion object{
        fun getDashPaint(): Paint {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//            paint.color = Color.WHITE
            paint.color = 0xa3000000.toInt()
            paint.style = Paint.Style.FILL
//            paint.style = Paint.Style.STROKE

            paint.strokeWidth = 10f
//            paint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0F)
            paint.maskFilter = BlurMaskFilter(10F, BlurMaskFilter.Blur.SOLID)
            return paint
        }


        fun getScaleAnimation(): Animation {
            val scaleAnimation = ScaleAnimation(
                0.5f,
                1f,
                0.5f,
                1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            scaleAnimation.duration = 500
            scaleAnimation.interpolator = BounceInterpolator()
            return scaleAnimation
        }
    }
}