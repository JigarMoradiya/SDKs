package com.example.iiifa_fan_android.utils

import android.content.Context
import android.content.Intent
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.example.iiifa_fan_android.R

class NonUnderlinedClickableSpan(// Keyword or url
    var text: String, // 0-hashtag , 1- mention, 2- url link
    var type: Int, val context: Context
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        //adding colors
        if (type == 1) ds.color = context.getColor(
            R.color.colorAccent
        ) else if (type == 2) ds.color = context.getColor(
            R.color.colorAccent
        ) else ds.color = context.getColor(
            R.color.colorAccent
        )
        ds.isUnderlineText = false
    }

    override fun onClick(v: View) {
        if (type == 0) {
//            //pass hashtags to activity using intents
//            Intent intent = new Intent(context, HashtagActivity.class);
//            intent.putExtra("hashtag_name", text);
//            context.startActivity(intent);
        } else if (type == 1) {
            //do for mentions
        } else {
            // passing weblinks urls to webview activity
            //  OpenLinkDialogue(context, text).show()

        }
    }
}