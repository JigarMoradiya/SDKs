package com.example.iiifa_fan_android.utils

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.iiifa_fan_android.R
import com.mikhaellopez.circularimageview.CircularImageView
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

@BindingAdapter("icons")
fun setIcons(view: AppCompatImageView, icons: Int?) {
    icons?.let { view.setImageResource(it) }
}

@BindingAdapter("phoneNumber")
fun setPhoneNumber(view: AppCompatEditText, phoneNumber: String?) {
    if (!phoneNumber.isNullOrEmpty()){
        val phoneNumberUtil = PhoneNumberUtil.createInstance(view.context)
        try {
            val phonenumber = phoneNumberUtil.parse(phoneNumber, "")
            view.setText(phonenumber.nationalNumber.toString())
        } catch (e: NumberParseException) {
            e.printStackTrace()
        }
    }

}

@BindingAdapter("countryCode")
fun setCountryCode(view: CountryCodePicker, phoneNumber: String?) {
    if (!phoneNumber.isNullOrEmpty()){
        val phoneNumberUtil = PhoneNumberUtil.createInstance(view.context)
        try {
            val phonenumber = phoneNumberUtil.parse(phoneNumber, "")
            view.setCountryForPhoneCode(phonenumber.countryCode)
        } catch (e: NumberParseException) {
            e.printStackTrace()
        }
    }
}

@BindingAdapter("imgUrlCircular","placeHolder")
fun setImgUrlCircular(view: CircularImageView, profile_url: String?, placeHolder : Drawable?) {
    val context = view.context
    Glide.with(view.context)
        .load(profile_url)
        .apply(RequestOptions().placeholder(placeHolder?: ContextCompat.getDrawable(context,R.drawable.ic_default_user)))
        .into(view)

}