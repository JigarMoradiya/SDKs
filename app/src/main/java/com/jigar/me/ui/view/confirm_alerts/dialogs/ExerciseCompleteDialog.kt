package com.jigar.me.ui.view.confirm_alerts.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.local.data.ExerciseLevel
import com.jigar.me.data.local.data.ExerciseLevelDetail
import com.jigar.me.data.local.data.ExerciseList
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.databinding.DialogExerciseCompleteBinding
import com.jigar.me.ui.view.dashboard.fragments.exercise.adapter.ExerciseAdditionSubtractionResultAdapter
import com.jigar.me.ui.view.dashboard.fragments.exercise.adapter.ExerciseMultiplicationDivisionResultAdapter
import com.jigar.me.utils.extensions.*

object ExerciseCompleteDialog {

    var alertdialog: AlertDialog? = null

    fun showPopup(context: Context, listExercise : MutableList<ExerciseList>,prefManager : AppPreferencesHelper,
                  currentParentData: ExerciseLevel?,currentChildData : ExerciseLevelDetail?, listener: ExerciseCompleteDialogInterface) {

        val alertLayout = DialogExerciseCompleteBinding.inflate(context.layoutInflater,null,false)
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setView(alertLayout.root)

        var rightAnswer = 0
        listExercise.filter { it.userAnswer == it.answer }.also { rightAnswer = it.size }

        val percentage : Float = ((rightAnswer.toFloat() * 100) / listExercise.size.toFloat())
        val isClearExercises = prefManager.getCustomParamBoolean("Exercise_"+currentChildData?.id,false)
        if (percentage >= 80){
            val previousClearExercises = prefManager.getCustomParamInt("Exercise_count_"+currentChildData?.id,0)
            if (isClearExercises){
                alertLayout.tvResult.text = "Congratulations, you got $percentage to clear this exercise"
            }else{
                val totalClearExercise = (previousClearExercises + 1)
                prefManager.setCustomParamInt("Exercise_count_"+currentChildData?.id,totalClearExercise)
                if (totalClearExercise == 3){
                    prefManager.setCustomParamBoolean("Exercise_"+currentChildData?.id,true)
                    alertLayout.tvResult.text = "Congratulations, you clear this exercise 3 time in a row and get 3 star badge."
                }else if (totalClearExercise == 2){
                    alertLayout.tvResult.text = "Congratulations, clear one more time this exercise of 80% result and got 3 star badge."
                }else if (totalClearExercise == 1){
                    alertLayout.tvResult.text = "Congratulations, To get 3 star badge you need to clear this exercise 2 more time in row."
                }
            }

            if (percentage == 100F){
                val index = DataProvider.generateIndex(3)
                val msg = if (index == 0){
                    context.getString(R.string.unstoppable_great_job)
                }else if (index == 1){
                    context.getString(R.string.you_are_so_intelligent)
                }else { // index = 2
                    context.getString(R.string.you_are_so_genius)
                }
                alertLayout.tvResultTitle.text = msg

            }else if (percentage >= 90F){
                val index = DataProvider.generateIndex(3)
                val msg = if (index == 0){
                    context.getString(R.string.you_are_brilliant)
                }else if (index == 1){
                    context.getString(R.string.you_are_glorious)
                }else { // index = 2
                    context.getString(R.string.you_are_clever)
                }
                alertLayout.tvResultTitle.text = msg
            }else if (percentage >= 80F){
                val index = DataProvider.generateIndex(1)
                val msg = if (index == 0){
                    context.getString(R.string.you_are_on_the_right_track)
                }else { // index = 1
                    context.getString(R.string.you_are_on_the_right_track)
                }
                alertLayout.tvResultTitle.text = msg
            }
        }else{
            if (listExercise.size == 5){
                if (percentage >= 60F){
                    alertLayout.tvResultTitle.text = context.getString(R.string.not_bad)
                }
            }else{
                if (percentage >= 70F){
                    alertLayout.tvResultTitle.text = context.getString(R.string.not_bad)
                }
            }

            prefManager.setCustomParamInt("Exercise_count_"+currentChildData?.id,0)
        }

        if (listExercise.first().question.contains("x")){
            alertLayout.recyclerview.show()
            alertLayout.recyclerviewAddition.hide()
            if (listExercise.size > 5){
                alertLayout.recyclerview.layoutManager = GridLayoutManager(context,2)
            }else{
                alertLayout.recyclerview.layoutManager = GridLayoutManager(context,1)
            }

            val adapter = ExerciseMultiplicationDivisionResultAdapter(listExercise)
            alertLayout.recyclerview.adapter = adapter
        }else if (listExercise.first().question.contains("/")){
            alertLayout.recyclerview.show()
            alertLayout.recyclerviewAddition.hide()
            if (listExercise.size > 5){
                alertLayout.recyclerview.layoutManager = GridLayoutManager(context,2)
            }else{
                alertLayout.recyclerview.layoutManager = GridLayoutManager(context,1)
            }

            val adapter = ExerciseMultiplicationDivisionResultAdapter(listExercise)
            alertLayout.recyclerview.adapter = adapter
        }else{
            alertLayout.recyclerview.hide()
            alertLayout.recyclerviewAddition.show()
            alertLayout.recyclerviewAddition.layoutManager = GridLayoutManager(context,listExercise.size)
            val adapter = ExerciseAdditionSubtractionResultAdapter(listExercise)
            alertLayout.recyclerviewAddition.adapter = adapter
        }

        alertLayout.tvClose.onClick {
            alertdialog?.dismiss()
            listener.exerciseCompleteCloseDialog()
        }

        alertBuilder.setView(alertLayout.root)
        alertBuilder.setCancelable(false)
        alertdialog = alertBuilder.show()
        val windows = alertdialog?.window
        val colorD = ColorDrawable(Color.TRANSPARENT)
        val insetD = if (listExercise.first().question.contains("x")){
            if (listExercise.size > 5){
                InsetDrawable(colorD, 100.dp, 0, 100.dp, 0)
            }else{
                InsetDrawable(colorD,250.dp, 0, 250.dp, 0)
            }
        }else{
            if (listExercise.size > 5){
                InsetDrawable(colorD, 50.dp, 0, 50.dp, 0)
            }else{
                InsetDrawable(colorD,150.dp, 0, 150.dp, 0)
            }
        }


        windows?.setBackgroundDrawable(insetD)
        // Setting Animation for Appearing from Center
        windows?.attributes?.windowAnimations = R.style.DialogAppearFromCenter
        // Positioning it in Bottom Right
        val wlp = windows?.attributes
        wlp?.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.gravity = Gravity.CENTER
        windows?.attributes = wlp
        alertdialog?.show()
    }

    interface ExerciseCompleteDialogInterface {
        fun exerciseCompleteCloseDialog()
    }

}