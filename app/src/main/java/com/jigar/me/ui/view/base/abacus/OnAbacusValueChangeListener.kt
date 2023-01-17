package com.jigar.me.ui.view.base.abacus

import android.view.View

interface OnAbacusValueChangeListener {
    fun onAbacusValueChange(abacusView: View, sum: Float)
    fun onAbacusValueSubmit(sum: Float)
    fun onAbacusValueDotReset()
}