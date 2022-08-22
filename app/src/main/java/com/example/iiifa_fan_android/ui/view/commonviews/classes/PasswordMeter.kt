package com.example.iiifa_fan_android.ui.view.commonviews.classes

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.iiifa_fan_android.data.models.PasswordMeter
import com.example.iiifa_fan_android.data.models.PasswordMeterStatus
import com.example.iiifa_fan_android.ui.view.commonviews.adapters.PasswordMeterAdapter
import com.example.iiifa_fan_android.utils.Constants


class PasswordMeterClass(val context: Context) {


    private lateinit var passwordMeterAdapter: PasswordMeterAdapter
    val list = ArrayList<PasswordMeter>()


    public fun setAdapter(rvPasswordMeter: RecyclerView) {

        list.clear()
        list.add(PasswordMeter(System.currentTimeMillis().toString(), Constants.AT_LEAST_8, PasswordMeterStatus.NOTSELECTED))
        list.add(PasswordMeter(System.currentTimeMillis().toString(), Constants.UPPERCASE_CHAR, PasswordMeterStatus.NOTSELECTED))
        list.add(PasswordMeter(System.currentTimeMillis().toString(), Constants.LOWERCASE_CHAR, PasswordMeterStatus.NOTSELECTED))
        list.add(PasswordMeter(System.currentTimeMillis().toString(), Constants.NUMERIC, PasswordMeterStatus.NOTSELECTED))

        passwordMeterAdapter = PasswordMeterAdapter(context, list)
        rvPasswordMeter.adapter = passwordMeterAdapter

    }


    public fun setAllError(): Boolean {
        var allSelected = true
        for (i in list) {
            if (i.status != PasswordMeterStatus.SELECTED) {
                i.status = PasswordMeterStatus.ERROR
                allSelected = false
            }
        }

        passwordMeterAdapter.notifyDataSetChanged()
        return allSelected
    }


    public fun setResetAll() {
        var allSelected = true
        for (i in list) {
            i.status = PasswordMeterStatus.NOTSELECTED

        }

        passwordMeterAdapter.notifyDataSetChanged()
    }


    public fun atLeast8charSelected(selected: Boolean) {
        for (i in list) {
            if (i.name == Constants.AT_LEAST_8) {
                if (selected) {
                    i.status = PasswordMeterStatus.SELECTED
                } else {
                    i.status = PasswordMeterStatus.NOTSELECTED
                }
            }
        }

        passwordMeterAdapter.notifyDataSetChanged()
    }

    public fun capitalLetterSelected(selected: Boolean) {
        for (i in list) {
            if (i.name == Constants.UPPERCASE_CHAR) {
                if (selected) {
                    i.status = PasswordMeterStatus.SELECTED
                } else {
                    i.status = PasswordMeterStatus.NOTSELECTED
                }
            }
        }

        passwordMeterAdapter.notifyDataSetChanged()
    }

    public fun smallCharSelected(selected: Boolean) {
        for (i in list) {
            if (i.name == Constants.LOWERCASE_CHAR) {
                if (selected) {
                    i.status = PasswordMeterStatus.SELECTED
                } else {
                    i.status = PasswordMeterStatus.NOTSELECTED
                }
            }
        }

        passwordMeterAdapter.notifyDataSetChanged()
    }

    public fun numericCharSelected(selected: Boolean) {
        for (i in list) {
            if (i.name == Constants.NUMERIC) {
                if (selected) {
                    i.status = PasswordMeterStatus.SELECTED
                } else {
                    i.status = PasswordMeterStatus.NOTSELECTED
                }
            }
        }

        passwordMeterAdapter.notifyDataSetChanged()
    }

    public fun isAllSelected(): Boolean {
        var bool = true

        for (i in list) {
            if (i.status != PasswordMeterStatus.SELECTED) {
                bool = false
                break
            }
        }

        return bool
    }


}