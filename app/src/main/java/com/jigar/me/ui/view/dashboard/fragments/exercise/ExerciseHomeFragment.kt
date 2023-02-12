package com.jigar.me.ui.view.dashboard.fragments.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.data.local.data.ExerciseLevel
import com.jigar.me.data.local.data.ExerciseLevelDetail
import com.jigar.me.data.local.data.ExerciseList
import com.jigar.me.databinding.FragmentExerciseHomeBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusMasterBeadShiftListener
import com.jigar.me.ui.view.base.abacus.AbacusMasterView
import com.jigar.me.ui.view.base.abacus.OnAbacusValueChangeListener
import com.jigar.me.ui.view.dashboard.fragments.exercise.adapter.ExerciseAdditionSubtractionAdapter
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.ViewUtils
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseHomeFragment : BaseFragment(), AbacusMasterBeadShiftListener, OnAbacusValueChangeListener,
    ExerciseLevelPagerAdapter.OnItemClickListener {
    private lateinit var binding: FragmentExerciseHomeBinding
    private var valuesAnswer: Int = -1
    private var currentSumVal = 0f
    private var isPurchased = false
    private var theam = AppConstants.Settings.theam_Egg
    private var isResetRunning = false
    private lateinit var exerciseLevelPagerAdapter: ExerciseLevelPagerAdapter
    private lateinit var exerciseAdditionSubtractionAdapter: ExerciseAdditionSubtractionAdapter
    private var listExerciseAdditionSubtractionQuestion = arrayListOf<String>()
    private var listExerciseAdditionSubtraction : MutableList<ExerciseList> = arrayListOf()
    private var exercisePosition = 0
    private var currentLevel: ExerciseLevel? = null
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentExerciseHomeBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }

    private fun initViews() {
        exerciseAdditionSubtractionAdapter = ExerciseAdditionSubtractionAdapter(listExerciseAdditionSubtractionQuestion)
        binding.recyclerviewExercise.adapter = exerciseAdditionSubtractionAdapter

        exerciseLevelPagerAdapter = ExerciseLevelPagerAdapter(DataProvider.getExerciseList(requireContext()),this@ExerciseHomeFragment)
        binding.viewPager.adapter = exerciseLevelPagerAdapter
        binding.indicatorPager.attachToPager(binding.viewPager)
        setAbacus()
    }

    private fun initListener() {
        binding.ivReset.onClick { resetClick()}
        binding.txtNext.onClick {
            if (currentLevel?.id == "1"){
                if (exercisePosition < listExerciseAdditionSubtraction.lastIndex){
                    binding.ivReset.performClick()
                    listExerciseAdditionSubtraction[exercisePosition].userAnswer = binding.tvAnswer.text.toString()
                    exercisePosition++
                    setAddSubQuestion()
                }
            }
        }
    }

    override fun onExerciseStartClick(data: ExerciseLevel, child: ExerciseLevelDetail?) {
//        Log.e("jigarLogs","data = "+ Gson().toJson(data))
//        Log.e("jigarLogs","child = "+ Gson().toJson(child))
        child?.let {
            if (data.id == "1"){
                exercisePosition = 0
                listExerciseAdditionSubtraction = DataProvider.generateAdditionSubExercise(it)
                binding.linearExerciseAddSub.show()
                binding.linearLevel.hide()
                setAddSubQuestion()
            }
        }
    }

    private fun setAddSubQuestion() {
        if (listExerciseAdditionSubtraction.lastIndex >= exercisePosition){
            valuesAnswer = listExerciseAdditionSubtraction[exercisePosition].answer
            binding.txtQueLabel.text = "Q".plus((exercisePosition+1))
            val question = listExerciseAdditionSubtraction[exercisePosition].question.replace("+","+ ").replace("-","- ")
            val list = question.split(" ")
            list.map {
                listExerciseAdditionSubtractionQuestion.add(it)
            }
            exerciseAdditionSubtractionAdapter.notifyDataSetChanged()
        }

    }

    private fun resetClick() {
        if (!isResetRunning) {
            isResetRunning = true
            binding.ivReset.y = 0f
            binding.ivReset.animate().setDuration(200)
                .translationYBy((binding.ivReset.height / 2).toFloat()).withEndAction {
                    binding.ivReset.animate().setDuration(200)
                        .translationYBy((-binding.ivReset.height / 2).toFloat()).withEndAction {
                            isResetRunning = false
                        }.start()
                }.start()
            onAbacusValueDotReset()
        }
    }

    private fun setAbacus() {
        with(prefManager){
            isPurchased = (getCustomParam(AppConstants.Purchase.Purchase_All,"") == "Y"
                    || getCustomParam(AppConstants.Purchase.Purchase_Toddler_Single_digit_level1,"") == "Y")
            if (isPurchased){
                setCustomParam(AppConstants.Settings.TheamTempView,getCustomParam(AppConstants.Settings.Theam,AppConstants.Settings.theam_Default))
            }else{
                setCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)
            }
            theam = getCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)

        }
        if (theam == AppConstants.Settings.theam_shape) {
            binding.ivDivider.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black2))
        } else {
            binding.ivDivider.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorAccent_light))
        }
        val colSpacing: Int = if (theam.equals(AppConstants.Settings.theam_Default, ignoreCase = true)) {
            ViewUtils.convertDpToPixel(Constants.Col_Space_exercise_polygon,requireActivity())
        } else {
            ViewUtils.convertDpToPixel(Constants.Col_Space_exercise_default,requireActivity())
        }

        binding.abacusTop.setNoOfRowAndBeads(0, 7, 1, colSpacing)
        binding.abacusBottom.setNoOfRowAndBeads(0, 7, 4, colSpacing)

        binding.abacusTop.onBeadShiftListener = this
        binding.abacusBottom.onBeadShiftListener = this

        lifecycleScope.launch {
            delay(500)
            binding.relAbacus.show()
        }
    }

    // abacus Bead Shift Listener
    override fun onBeadShift(abacusView: AbacusMasterView, rowValue: IntArray) {
        val singleBeadWeight = abacusView.singleBeadValue
        var accumulator = 0

        when (abacusView.id) {
            R.id.abacusTop -> if (binding.abacusBottom.engine != null) {
                val bottomVal = binding.abacusBottom.engine!!.getValue()
                var i = 0
                while (i < rowValue.size) {
                    accumulator *= 10
                    val rval = rowValue[i]
                    if (rval > -1) accumulator += rval * singleBeadWeight
                    i++
                }
                val intSumVal = bottomVal + accumulator
                val strCurVal = intSumVal.toString()
                currentSumVal = java.lang.Float.valueOf(strCurVal)
                binding.tvCurrentVal.text = strCurVal
                binding.tvAnswer.text = strCurVal
                onAbacusValueChange(abacusView, currentSumVal)
            }
            R.id.abacusBottom -> if (binding.abacusTop.engine != null) {
                val topVal = binding.abacusTop.engine!!.getValue()
                var i = 0
                while (i < rowValue.size) {
                    accumulator *= 10
                    val rval = rowValue[i]
                    if (rval > -1) accumulator += rval * singleBeadWeight
                    i++
                }
                val intSumVal = topVal + accumulator
                val strCurVal = intSumVal.toString()
                currentSumVal = java.lang.Float.valueOf(strCurVal)
                binding.tvCurrentVal.text = strCurVal
                binding.tvAnswer.text = strCurVal
                onAbacusValueChange(abacusView, currentSumVal)
            }
        }
    }

    override fun onAbacusValueChange(abacusView: View, sum: Float) {
        with(prefManager){
            if (!binding.linearLevel.isVisible){
                Log.e("jigarLogs","sum = "+sum+" valuesAnswer = "+valuesAnswer)
                if (sum.toInt() == valuesAnswer) {

                }
            }

        }

    }

    override fun onAbacusValueSubmit(sum: Float) {

    }

    override fun onAbacusValueDotReset() {
        resetAbacus()
    }

    private fun resetAbacus() {
        binding.abacusTop.reset()
        binding.abacusBottom.reset()
    }


}