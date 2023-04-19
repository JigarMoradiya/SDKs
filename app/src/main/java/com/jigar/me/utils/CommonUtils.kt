package com.jigar.me.utils

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.extensions.show
import org.json.JSONException
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat


object CommonUtils {
    fun mixTwoColors(color1: Int, color2: Int, amount: Float): Int {
        val ALPHA_CHANNEL: Byte = 24
        val RED_CHANNEL: Byte = 16
        val GREEN_CHANNEL: Byte = 8
        //final byte BLUE_CHANNEL = 0;
        val inverseAmount = 1.0f - amount
        val r = ((color1 shr RED_CHANNEL.toInt() and 0xff).toFloat() * amount + (color2 shr RED_CHANNEL.toInt() and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val g = ((color1 shr GREEN_CHANNEL.toInt() and 0xff).toFloat() * amount + (color2 shr GREEN_CHANNEL.toInt() and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val b = ((color1 and 0xff).toFloat() * amount + (color2 and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        return 0xff shl ALPHA_CHANNEL.toInt() or (r shl RED_CHANNEL.toInt()) or (g shl GREEN_CHANNEL.toInt()) or b
    }
    fun removeTrailingZero(formattingInput: String): String {
        if (!formattingInput.contains(".")) {
            return formattingInput
        }
        val dotPosition = formattingInput.indexOf(".")
        val newValue = formattingInput.substring(dotPosition, formattingInput.length)
        return if (newValue == ".0") {
            formattingInput.substring(0, dotPosition)
        } else formattingInput
    }
    fun blinkView(view: View, repeatCount : Int? = null){
        view.show()
        val animation: Animation = AlphaAnimation(1F, AppConstants.BLINK_ICON_ANIMATION_ALPHA) //to change visibility from visible to invisible
        animation.duration = AppConstants.BLINK_ICON_ANIMATION_DURATION //duration for each animation cycle
        animation.interpolator = LinearInterpolator()
        if (repeatCount == null){
            animation.repeatCount = Animation.INFINITE //repeating indefinitely
        }else{
            animation.repeatCount = repeatCount
        }
        animation.repeatMode = Animation.REVERSE //animation will start from end point once ended.
        view.startAnimation(animation) //to start animation
    }
    fun AppPreferencesHelper.getCurrentSumFromPref(pageId : String) : Int? {
        var currentPos : Int? = null
        try {
            val pageSum: String = getCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM, "{}")
            val objJson = JSONObject(pageSum)
            if (objJson.has(pageId)) {
                currentPos = objJson.getInt(pageId)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return currentPos
    }
    fun Context.getCurrentSumFromPref(pageId : String) : Int? {
        var currentPos : Int? = null
        try {
            val pageSum: String = AppPreferencesHelper(this, AppConstants.PREF_NAME)
                .getCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM, "{}")
            val objJson = JSONObject(pageSum)
            if (objJson.has(pageId)) {
                currentPos = objJson.getInt(pageId)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return currentPos
    }
    fun AppPreferencesHelper.saveCurrentSum(pageId : String, current_pos : Int) {
        try {
            val pageSum: String = getCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM, "{}")
            val objJson = JSONObject(pageSum)
            objJson.put(pageId, current_pos)
            setCustomParam(AppConstants.AbacusProgress.PREF_PAGE_SUM,objJson.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    private fun get2Decimal(value: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(value)
    }
}