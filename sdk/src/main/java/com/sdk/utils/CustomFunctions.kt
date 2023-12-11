package com.sdk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sdk.BuildConfig
import com.sdk.R
import com.sdk.ui.interfaces.RemoveTagClickListner
import com.sdk.utils.extensions.onClick
import org.apache.commons.text.StringEscapeUtils


object CustomFunctions {
    //Decode string
    val keyThree: String
        get() {
            return String(Base64.decode(BuildConfig.CONSTANT_KEY,Base64.DEFAULT))
        }
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
//        return Settings.Secure.getString(ConnectedMindSDK().instance?.contentResolver, Settings.Secure.ANDROID_ID)
        return "sdk_user"
    }

    @JvmStatic
    fun decodeMessage(message: String?): String {
        return try {
            StringEscapeUtils.unescapeJava(message)
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    @JvmStatic
    fun setCategoryChips(
        context: Context,
        categorys: ArrayList<com.sdk.data.Chip>,
        chipGroup: ChipGroup,
        isCancelable: Boolean = false,
        removeTagClickListner: RemoveTagClickListner? = null
    ) {
        chipGroup.removeAllViews()
        for (category in categorys) {
            val mChip = LayoutInflater.from(context).inflate(R.layout.layout_chip, null, false) as Chip
            mChip.text = category.name

            mChip.chipBackgroundColor = (context.buildColorStateList(category.color,category.color))
            val paddingDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f,context.resources.displayMetrics).toInt()
            mChip.setPadding(paddingDp, 0, paddingDp, 0)

            if (isCancelable) {
                mChip.isCloseIconVisible = true
            }

            mChip.onClick {
                removeTagClickListner?.onItemClicked(category)
            }

            mChip.setOnCloseIconClickListener {
                removeTagClickListner?.onRemoveClicked(category)
                chipGroup.removeView(mChip)
            }

            chipGroup.addView(mChip)
        }
    }

    private fun Context.buildColorStateList(
        pressedColorAttr: String? = "#825ffc",
        defaultColorAttr: String? = "#825ffc"
    ): ColorStateList {
        val pressedColor: Int = Color.parseColor(pressedColorAttr)
        val defaultColor: Int = Color.parseColor(defaultColorAttr)
        return ColorStateList(arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(pressedColor,defaultColor))
    }

    fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
        val a = 255.coerceAtMost(0.coerceAtLeast((alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        return a + rgb
    }
}