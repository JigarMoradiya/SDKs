package com.jigar.me.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.jigar.me.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("icons")
fun setIcons(view: AppCompatImageView, icons: Int?) {
    icons?.let { view.setImageResource(it) }
}

@BindingAdapter("imgUrl","placeHolder")
fun setImgUrl(view: AppCompatImageView, profile_url: String?, placeHolder : Drawable?) {
    val context = view.context
    Glide.with(view.context)
        .load(profile_url)
        .apply(RequestOptions().placeholder(placeHolder?: ContextCompat.getDrawable(context, R.drawable.ic_default_user)))
        .into(view)
}
@BindingAdapter("loadImgInsideCache","placeHolder")
fun loadImgInsideCache(view: AppCompatImageView, loadImgURL: String?,placeHolder : Drawable) {
    if (loadImgURL.isNullOrEmpty()) {
        view.setImageDrawable(placeHolder)
    } else {
        Glide.with(view.context)
            .load(loadImgURL)
            .placeholder(ImageUtils.getCircleProgress(view.context))
            .error(placeHolder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(view)
    }

}

@BindingAdapter("cardColor")
fun cardBackgroundColor(view: MaterialCardView, cardColor: Int) {
    view.setCardBackgroundColor(ColorStateList.valueOf(cardColor))
}
@BindingAdapter("txtColorApp")
fun txtColorApp(view: MaterialTextView, tintColor: Int?) {
    view.setTextColor(ColorStateList.valueOf(tintColor!!))
}
@BindingAdapter("purchaseOrderId")
fun purchaseOrderId(view: MaterialTextView, orderId: String) {
    view.text = HtmlCompat.fromHtml("<b>Order Id : </b>$orderId", HtmlCompat.FROM_HTML_MODE_COMPACT)
}
@BindingAdapter("purchaseTime")
fun purchaseTime(view: MaterialTextView, purchaseTime: Long) {
    if (purchaseTime > 0) {
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = purchaseTime
        view.text = HtmlCompat.fromHtml("<b>Purchased ON : </b>${formatter.format(calendar.time)}",HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
}