package com.example.iiifa_fan_android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.utils.NoChangingBackgroundTextInputLayout
import com.example.iiifa_fan_android.utils.PrefManager
import com.example.iiifa_fan_android.ui.view.commonviews.classes.LoadingUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson


object CustomViews {
    // static ProgressDialog progressDialog;
    private var dialog: Dialog? = null

    @JvmStatic
    open fun showInputMethod(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, 0)
    }


    //Hide keyboard if open
    @JvmStatic
    open fun hideKeyBoard(activity: Activity?) {
        val inputManager = activity?.getSystemService(
            android.content.Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        val focusedView: View? = activity?.currentFocus
        /*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dmitriev
         */if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(
                focusedView.getWindowToken(),
                android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }


    @JvmStatic
    fun showSuccessToast(inflater: LayoutInflater, text: String?) {
        //  LayoutInflater inflater = context.getLayoutInflater();
        val layout = inflater.inflate(R.layout.toast_layout_success, null)
        val tvSucces = layout.findViewById<TextView>(R.id.tv_success)
        tvSucces.text = text
        val toast =
            Toast(com.example.iiifa_fan_android.utils.MyApplication.getInstance().applicationContext)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 20)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    @JvmStatic
    fun showFailToast(inflater: LayoutInflater, text: String?) {
        if (text != null && !TextUtils.isEmpty(text)) {
            //  LayoutInflater inflater = context.getLayoutInflater();
            val layout = inflater.inflate(R.layout.toast_layout_fail, null)
            val tvSucces = layout.findViewById<TextView>(R.id.tv_fail)
            tvSucces.text = text
            val toast =
                Toast(com.example.iiifa_fan_android.utils.MyApplication.getInstance().applicationContext)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 20)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            if (!text.isNullOrEmpty())
                toast.show()
        }
    }


    @SuppressLint("RestrictedApi")
    @JvmStatic
    fun setErrortoEditText(
        context: Context?,
        appCompatEditText: AppCompatEditText,
        textInputLayout: NoChangingBackgroundTextInputLayout,
        validation_message: String?
    ) {
        if (appCompatEditText.tag != null && !TextUtils.isEmpty(appCompatEditText.tag.toString())
            && appCompatEditText.tag.toString()
                .equals(Constants.DONT_DISPLAY_ERROR_ICON, ignoreCase = true)
        ) {
        } else {

        }


        appCompatEditText.setBackgroundResource(R.drawable.text_box_underline_error)
        textInputLayout.error = validation_message
        textInputLayout.requestFocus()
    }

    @JvmStatic
    fun removeError(
        context: Context?,
        appCompatEditText: AppCompatEditText,
        textInputLayout: NoChangingBackgroundTextInputLayout,
        drawable: Int = 0,
        hintColor: Int = R.color.colorAccent,
        isDropDown: Boolean = false
    ) {
        //       appCompatEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawable, 0)
//        appCompatEditText.setHintTextColor(
//            ContextCompat.getColorStateList(
//                context!!,
//                hintColor
//            )
//        )

        if (!isDropDown)
            appCompatEditText.setBackgroundResource(R.drawable.text_box_underline)
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false
    }

    @JvmStatic
    fun startButtonLoading(context: Context, isCancelable: Boolean = true) {
        LoadingUtils.showDialog(context, isCancelable!!)
    }

    @JvmStatic
    fun isAlreadyLoading(): Boolean {
        return LoadingUtils.isAlreadyLoading()
    }


    @JvmStatic
    fun hideButtonLoading() {
        LoadingUtils.hideDialog()
//        if (dialog != null) {
//            val context = (dialog?.context as ContextWrapper).baseContext
//            if (!(context as Activity).isFinishing && !context.isDestroyed) dialog?.dismiss()
//            dialog = null
//        }
    }


    @JvmStatic
    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
    }


    fun buildColorStateList(
        context: Context?,
        pressedColorAttr: String? = "#825ffc",
        defaultColorAttr: String? = "#825ffc"
    ): ColorStateList {


        Log.d("hex_code", pressedColorAttr + " " + defaultColorAttr)

        val pressedColor: Int = android.graphics.Color.parseColor(pressedColorAttr)
        val defaultColor: Int = android.graphics.Color.parseColor(defaultColorAttr)


        return ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(
                pressedColor,
                defaultColor
            )
        )

    }

    @JvmStatic
    fun getBackGround(
        context: Context,
        bgColor: Int,
        borderColor: Int
    ): MaterialShapeDrawable {
        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, 15.toFloat())
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        shapeDrawable.fillColor = ContextCompat.getColorStateList(context, bgColor)
        shapeDrawable.setStroke(1.0f, ContextCompat.getColor(context, borderColor))

        return shapeDrawable
    }


    @JvmStatic
    fun isUserLoggedIn(context: Activity): Boolean {
        var login = true
        val prefManager = PrefManager(context)

        if (prefManager.user.isNullOrEmpty()) {
            login = false
            openLoginActivity(context)
        }

        return login
    }


    fun openLoginActivity(context: Activity) {
//        val intent = LoginActivity.getInstance(context, true)
//        (context).startActivity(intent)
    }

}

fun Float.toDips() =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        MyApplication.getInstance().resources.displayMetrics
    );