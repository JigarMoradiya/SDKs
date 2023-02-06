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