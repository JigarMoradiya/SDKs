package com.jigar.me.ui.view.dashboard.fragments.exam.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jigar.me.R
import com.jigar.me.databinding.FragmentExamHomeBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.toastL
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ExamHomeFragment : BaseFragment() {

    private lateinit var binding: FragmentExamHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExamHomeBinding.inflate(inflater, container, false)
        init()
        clickListener()
        return binding.root
    }

    private fun init() {

    }

    private fun clickListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.cardExamHistory.onClick { mNavController.navigate(R.id.action_examHomeFragment_to_examHistoryHomeFragment) }
        binding.txtStartExam.onClick { onExamStartClick() }
    }

    private fun onExamStartClick() {
        var level = ""
        var levelLable = ""
        when {
            binding.rdchildLevelBeginner.isChecked -> {
                level = Constants.examLevelBeginnerValue
                levelLable = Constants.examLevelBeginner
            }
            binding.rdchildLevelIntermediate.isChecked -> {
                level = Constants.examLevelIntermediateValue
                levelLable = Constants.examLevelIntermediate
            }
            binding.rdchildLevelExpert.isChecked -> {
                level = Constants.examLevelExpertValue
                levelLable = Constants.examLevelExpert
            }
        }
        if (level.isEmpty()){
            requireContext().toastL(getString(R.string.child_level))
        }else{
            if (level == Constants.examLevelBeginnerValue){
                val action = ExamHomeFragmentDirections.actionExamHomeFragmentToLevel1ExamFragment(level,levelLable)
                mNavController.navigate(action)
            }else{
                val action = ExamHomeFragmentDirections.actionExamHomeFragmentToExamFragment(level,levelLable)
                mNavController.navigate(action)
            }
        }
    }

}