package com.jigar.me.ui.view.base.abacus

import com.jigar.me.data.local.data.AbacusBeadType
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.ViewUtils

object AbacusUtils {
    fun setAbacusColumn(prefManager : AppPreferencesHelper,abacusType : AbacusBeadType, abacusTop1: AbacusMasterView, abacusBottom1: AbacusMasterView, abacusTop2: AbacusMasterView? = null, abacusBottom2: AbacusMasterView? = null) : String{
        var theam : String
        with(prefManager){
            val isPurchased = (getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y")
            if (isPurchased){
                setCustomParam(AppConstants.Settings.TheamTempView,DataProvider.getAbacusThemeList().first())
            }else{
                setCustomParam(AppConstants.Settings.TheamTempView, AppConstants.Settings.theam_Default)
            }
            theam = getCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)
        }
        setAbacusColumnTheme(theam,abacusType,abacusTop1,abacusBottom1, abacusTop2, abacusBottom2)
        return theam
    }

    fun setAbacusColumnTheme(theme : String,abacusType : AbacusBeadType, abacusTop1: AbacusMasterView, abacusBottom1: AbacusMasterView, abacusTop2: AbacusMasterView? = null, abacusBottom2: AbacusMasterView? = null){
        val colSpacing: Int = if (abacusType == AbacusBeadType.Exam){
            if (theme.equals(AppConstants.Settings.theam_Default, ignoreCase = true)) {
                ViewUtils.convertDpToPixel(Constants.Col_Space_exam_polygon,abacusTop1.context)
            } else if (theme.equals(AppConstants.Settings.theam_Egg, ignoreCase = true)) {
                ViewUtils.convertDpToPixel(Constants.Col_Space_exam_egg,abacusTop1.context)
            } else {
                ViewUtils.convertDpToPixel(Constants.Col_Space_exam_default,abacusTop1.context)
            }
        }else{
            if (theme.equals(AppConstants.Settings.theam_Default, ignoreCase = true)) {
                ViewUtils.convertDpToPixel(Constants.Col_Space_exam_result_polygon,abacusTop1.context)
            } else if (theme.equals(AppConstants.Settings.theam_Egg, ignoreCase = true)) {
                ViewUtils.convertDpToPixel(Constants.Col_Space_exam_result_egg,abacusTop1.context)
            } else {
                ViewUtils.convertDpToPixel(Constants.Col_Space_exam_result_default,abacusTop1.context)
            }
        }
        abacusTop1.setNoOfRowAndBeads(0, 3, 1, colSpacing,abacusType)
        abacusBottom1.setNoOfRowAndBeads(0, 3, 4, colSpacing,abacusType)

        if (abacusTop2 != null && abacusBottom2 != null){
            abacusTop2.setNoOfRowAndBeads(0, 3, 1, colSpacing,abacusType)
            abacusBottom2.setNoOfRowAndBeads(0, 3, 4, colSpacing,abacusType)
        }
    }

    fun setNumber(questionTemp: String,abacusTop1: AbacusMasterView,abacusBottom1: AbacusMasterView,questionTemp2: String? = null,abacusTop2: AbacusMasterView? = null,abacusBottom2: AbacusMasterView? = null) {
        abacusTop1.postDelayed({
            setBead(questionTemp,abacusTop1,abacusBottom1)
        },0)

        if (questionTemp2 != null && abacusTop2 != null && abacusBottom2 != null){
            abacusTop2.postDelayed({
                setBead(questionTemp2,abacusTop2,abacusBottom2)
            },0)
        }
    }

    private fun setBead(questionTemp: String,abacusTop: AbacusMasterView, abacusBottom: AbacusMasterView) {
        val topPositions = ArrayList<Int>()
        val bottomPositions = ArrayList<Int>()
        val totalLength = 3
        val remainLength = totalLength - questionTemp.length
        var zero = ""
        for (i in 1..remainLength){
            zero += "0"
        }

        val question = zero+questionTemp
        for (i in 0 until if (totalLength == 1) 2 else totalLength) {
            if (i < question.length) {
                val charAt = question[i] - '1' //convert char to int. minus 1 from question as in abacuse 0 item have 1 value.
                if (charAt >= 0) {
                    if (charAt >= 4) {
                        topPositions.add(i, 0)
                        bottomPositions.add(i, charAt - 5)
                    } else {
                        topPositions.add(i, -1)
                        bottomPositions.add(i, charAt)
                    }
                } else {
                    topPositions.add(i, -1)
                    bottomPositions.add(i, -1)
                }
            } else {
                topPositions.add(i, -1)
                bottomPositions.add(i, -1)
            }
        }
        val subTop: MutableList<Int> = ArrayList()
        subTop.addAll(topPositions.subList(0, question.length))
        val subBottom: MutableList<Int> = ArrayList()
        subBottom.addAll(bottomPositions.subList(0, question.length))
        for (i in question.indices) {
            topPositions.removeAt(0)
            bottomPositions.removeAt(0)
        }
        topPositions.addAll(subTop)
        bottomPositions.addAll(subBottom)

        //app was crashing if position set before update no of row count. so added this delay.
        abacusTop.post {
            abacusTop.setSelectedPositions(topPositions,null)
        }
        abacusBottom.post {
            abacusBottom.setSelectedPositions(bottomPositions,null)
        }
    }
}