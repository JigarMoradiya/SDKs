package com.jigar.me.utils.extensions

import android.os.Bundle
import com.google.gson.Gson

fun String?.isStringNotBlank() = this!=null && this.isNotBlank()
fun Collection<Any?>?.isNotNullOrEmpty() = this!=null && this.isNotEmpty()
fun Collection<Any?>?.isEmpty() = this!=null && this.isEmpty()
