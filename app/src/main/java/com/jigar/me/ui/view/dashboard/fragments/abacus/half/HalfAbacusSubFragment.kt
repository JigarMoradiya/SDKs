package com.jigar.me.ui.view.dashboard.fragments.abacus.half

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jigar.me.R
import com.jigar.me.data.local.data.AbacusBeadType
import com.jigar.me.data.local.data.DataProvider
import com.jigar.me.databinding.FragmentAbacusSubBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.base.abacus.AbacusMasterBeadShiftListener
import com.jigar.me.ui.view.base.abacus.AbacusMasterCompleteListener
import com.jigar.me.ui.view.base.abacus.AbacusMasterView
import com.jigar.me.ui.view.base.abacus.OnAbacusValueChangeListener
import com.jigar.me.utils.*
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.setAbacusResetShakeAnimation
import com.jigar.me.utils.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HalfAbacusSubFragment : BaseFragment(), AbacusMasterBeadShiftListener {
    private lateinit var binding: FragmentAbacusSubBinding
    // Settings Constants
    private var isDisplayAbacusNumber = true

    private var abacus_type = 0 // 0 = sum-sub-single  1 = multiplication 2 = divide
    private var final_column = 0
    private var noOfDecimalPlace = 0

    // abacus move
    private var isResetRunning: Boolean = false
    private var currentSumVal: Float = 0f
    private var resetX: Float = 0f
    var questionLength = 0
    var finalAnsLength: Int = 0

    // for division
    private var topSelectedPositions: ArrayList<Int> = arrayListOf()
    private var bottomSelectedPositions: ArrayList<Int> = arrayListOf()

    private var onAbacusValueChangeListener: OnAbacusValueChangeListener? = null

    fun newInstance(column: Int, noOfDecimalPlace: Int, abacus_type: Int): HalfAbacusSubFragment {
        val fragment = HalfAbacusSubFragment()
        fragment.final_column = column
        fragment.noOfDecimalPlace = noOfDecimalPlace
        fragment.abacus_type = abacus_type
        return fragment
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAbacusSubBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }

    private fun initViews() {
        isDisplayAbacusNumber = prefManager.getCustomParamBoolean(AppConstants.Settings.Setting_display_abacus_number, true)

        val theme = prefManager.getCustomParam(AppConstants.Settings.TheamTempView,AppConstants.Settings.theam_Default)
        val themeContent = DataProvider.findAbacusThemeType(requireContext(),theme,AbacusBeadType.None)
        themeContent.abacusFrame135.let { binding.rlAbacusMain.setBackgroundResource(it) }
        themeContent.dividerColor1.let { binding.ivDivider.setBackgroundColor(ContextCompat.getColor(requireContext(),it)) }
        themeContent.resetBtnColor8.let {
            binding.ivReset.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.ivRight.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.ivLeft.setColorFilter(ContextCompat.getColor(requireContext(),it), android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    private fun initListener() {
        binding.ivReset.onClick { onResetClick() }
        binding.resettoContinue.onClick { onResetClick() }
    }
    fun setOnAbacusValueChangeListener(onAbacusValueChangeListener: OnAbacusValueChangeListener?) {
        this.onAbacusValueChangeListener = onAbacusValueChangeListener
    }

    // TODO abacus Move logic
    override fun onResume() {
        if (isAdded) {
            setBead()
        }
        if (abacus_type == 2) {
            setSelectedPositions(topSelectedPositions, bottomSelectedPositions, null)
        }
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding.abacusTop.stop()
        binding.abacusBottom.stop()
    }

    private fun setBead() {
        binding.abacusTop.setNoOfRowAndBeads(0, final_column, 1)
        binding.abacusBottom.setNoOfRowAndBeads(0, final_column, 4)

        binding.abacusTop.onBeadShiftListener = this
        binding.abacusBottom.onBeadShiftListener = this
    }

    override fun onBeadShift(abacusView: AbacusMasterView, rowValue: IntArray) {
        val singleBeadWeight = abacusView.singleBeadValue
        var accumulator = 0

        if (noOfDecimalPlace == 0) {
            when (abacusView.id) {
                R.id.abacusTop -> if (binding.abacusBottom.engine != null && isVisible) {
                    val bottomVal = binding.abacusBottom.engine!!.getValue()
                    var i = 0
                    while (i < rowValue.size) {
                        accumulator *= 10
                        val rval = rowValue[i]
                        if (rval > -1) accumulator += rval * singleBeadWeight
                        i++
                    }
                    currentSumVal = (bottomVal + accumulator).toFloat()
                    if (abacus_type == 0 || abacus_type == 1) {
                        setCurrentValue(currentSumVal.toInt().toString())
                        onAbacusValueChangeListener?.onAbacusValueChange(
                            abacusView,
                            currentSumVal
                        )
                    }
                }
                R.id.abacusBottom -> if (binding.abacusTop.engine != null && isVisible) {
                    val topVal = binding.abacusTop.engine!!.getValue()
                    var i = 0
                    while (i < rowValue.size) {
                        accumulator *= 10
                        val rval = rowValue[i]
                        if (rval > -1) accumulator += rval * singleBeadWeight
                        i++
                    }
                    currentSumVal = (topVal + accumulator).toFloat()
                    if (abacus_type == 0 || abacus_type == 1) {
                        setCurrentValue(currentSumVal.toInt().toString())
                        try {
                            onAbacusValueChangeListener?.onAbacusValueChange(
                                abacusView,
                                currentSumVal
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            if (abacus_type == 2) {
                /*common code to calculate sumvalue*/
                var currentAns: String = currentSumVal.toInt().toString()
                val iterationCount = questionLength + finalAnsLength - 1 - currentAns.length
                for (i in 0 until iterationCount) {
                    currentAns = "0$currentAns"
                }
                if (questionLength + finalAnsLength - 1 <= currentAns.length) {
                    try {
                        val right = Integer.valueOf(
                            currentAns.substring(
                                currentAns.length - questionLength,
                                currentAns.length
                            )
                        )
                        val left = Integer.valueOf(currentAns.substring(0, finalAnsLength))
                        setCurrentValue("$left < $right")
                    } catch (e: Exception) {
                    }
                } else {
                    setCurrentValue(currentSumVal.toInt().toString())
                }
                onAbacusValueChangeListener?.onAbacusValueChange(abacusView, currentSumVal)
            }
        } else {
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
                    var strCurVal = intSumVal.toString()
                    strCurVal = if (strCurVal.length < noOfDecimalPlace) {
                        val preFix =
                            "0." + String(CharArray(noOfDecimalPlace - strCurVal.length)).replace(
                                '\u0000',
                                '0')
                        preFix + strCurVal
                    } else if (strCurVal.length == noOfDecimalPlace) {
                        "0.$strCurVal"
                    } else {
                        strCurVal.substring(
                            0,strCurVal.length - noOfDecimalPlace) + "." + strCurVal.substring(
                            strCurVal.length - noOfDecimalPlace,
                            strCurVal.length)
                    }
                    currentSumVal = java.lang.Float.valueOf(strCurVal)
                    setCurrentValue(strCurVal)
                    try {
                        onAbacusValueChangeListener?.onAbacusValueChange(abacusView, currentSumVal)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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
                    var strCurVal = intSumVal.toString()
                    strCurVal = if (strCurVal.length < noOfDecimalPlace) {
                        val preFix =
                            "0." + String(CharArray(noOfDecimalPlace - strCurVal.length)).replace(
                                '\u0000',
                                '0'
                            )
                        preFix + strCurVal
                    } else if (strCurVal.length == noOfDecimalPlace) {
                        "0.$strCurVal"
                    } else {
                        strCurVal.substring(
                            0,
                            strCurVal.length - noOfDecimalPlace
                        ) + "." + strCurVal.substring(
                            strCurVal.length - noOfDecimalPlace,
                            strCurVal.length
                        )
                    }
                    currentSumVal = java.lang.Float.valueOf(strCurVal)
                    setCurrentValue(strCurVal)
                    try {
                        onAbacusValueChangeListener?.onAbacusValueChange(abacusView, currentSumVal)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun resetAbacus() {
        binding.abacusTop.reset()
        binding.abacusBottom.reset()
        showResetToContinue(false)
    }

    fun resetButtonEnable(isEnable: Boolean) {
        binding.ivReset.isEnabled = isEnable
        binding.ivReset.isClickable = isEnable
    }
    fun showResetToContinue(type: Boolean) {
        if (type) {
            binding.resettoContinue.show()
            startTimerForToolTips()
//            if (prefManager.getCustomParamBoolean(AppConstants.Settings.Setting_left_hand, true)){
//                startTimerForToolTips()
//            }else{
//                CommonUtils.blinkView(binding.ivReset,3)
//            }
        } else {
            binding.resettoContinue.hide()
        }
    }

    private fun startTimerForToolTips() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded && isResumed){
                binding.ivReset.setAbacusResetShakeAnimation()
            }
        },2500)
    }

    private fun onResetClick() {
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
            onAbacusValueChangeListener?.onAbacusValueDotReset()
        }
    }

    // TODO for Division
    fun setSelectedPositions(
        topSelectedPositions: ArrayList<Int>,
        bottomSelectedPositions: ArrayList<Int>,
        setPositionCompleteListener: AbacusMasterCompleteListener?
    ) {
        this.topSelectedPositions = topSelectedPositions
        this.bottomSelectedPositions = bottomSelectedPositions

        if (isAdded) {
            //app was crashing if position set before update no of row count. so added this delay.
            binding.abacusBottom.post {
                binding.abacusTop.setSelectedPositions(
                    topSelectedPositions,
                    setPositionCompleteListener
                )
                binding.abacusBottom.setSelectedPositions(
                    bottomSelectedPositions,
                    setPositionCompleteListener
                )
            }
        }
    }

    fun setQuestionAndDividerLength(questionLength: Int, finalAnsLength: Int) {
        this.questionLength = questionLength
        this.finalAnsLength = finalAnsLength
        final_column = questionLength + finalAnsLength - 1
        final_column = if (final_column == 1) 2 else final_column
        setAbacusRowCountAndInvalidate(final_column)
    }

    fun setAbacusRowCountAndInvalidate(column: Int) {
        final_column = column
        if (isAdded) {
            binding.abacusTop.noOfColumn = column
            //            binding.abacusTop.setSelectedPositions(topSelectedPositions, null);
            binding.abacusBottom.noOfColumn = column
            //            binding.abacusBottom.setSelectedPositions(bottomSelectedPositions, null);
        }
    }

    private fun setCurrentValue(strCurVal: String) {
        when {
            isDisplayAbacusNumber -> {
                binding.tvCurrentVal.show()
                binding.tvCurrentVal.text = strCurVal
            }
            else -> {
                binding.tvCurrentVal.visibility = View.INVISIBLE
            }
        }
    }
}