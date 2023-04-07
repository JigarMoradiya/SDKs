package com.jigar.me.ui.view.dashboard.fragments.exam.result

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.local.data.AbacusBeadType
import com.jigar.me.data.local.data.BeginnerExamPaper
import com.jigar.me.data.local.data.BeginnerExamQuestionType
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.databinding.RawExamResultLevel1AbacusBinding
import com.jigar.me.databinding.RawExamResultLevel1Binding
import com.jigar.me.ui.view.base.abacus.AbacusUtils
import com.jigar.me.utils.Calculator
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExamResultLevel1Adapter(
    private var listData: List<BeginnerExamPaper>,
    private val examAbacusTheme: String,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mCalculator: Calculator = Calculator()

    override fun getItemViewType(position: Int): Int {
        val data = listData[position]
        if (data.isAbacusQuestion == true){
            return 0
        }else{
            return 1
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == 0){
            val binding = RawExamResultLevel1AbacusBinding.inflate(parent.context.layoutInflater,parent,false)
            return ViewHolderAbacus(binding)
        }else{
            val binding = RawExamResultLevel1Binding.inflate(parent.context.layoutInflater,parent,false)
            return FormViewHolder(binding)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = listData[position]
        when(holder.itemViewType){
            0 ->{ // abacus
                with(holder as ViewHolderAbacus){
                    val context = holder.mBinding.root.context

                    if (data.userAnswer?.isEmpty() == true) {
                        mBinding.img.hide()
                        mBinding.txtYourAnswer.show()
                        mBinding.txtYourAnswer.text =
                            mBinding.txtYourAnswer.context.getText(R.string.SKipped)
                    } else {
                        mBinding.img.show()
                        val tempAns = when (data.type) {
                            BeginnerExamQuestionType.Additions -> {
                                data.value+"+"+data.value2
                            }
                            BeginnerExamQuestionType.Subtractions -> {
                                data.value+"-"+data.value2
                            }
                            else -> {
                                data.value
                            }
                        }
                        val resultObject = mCalculator.getResult(tempAns,tempAns)
                        val correctAns = CommonUtils.removeTrailingZero(resultObject)

                        if (correctAns.equals(data.userAnswer, ignoreCase = true)) {
                            mBinding.img.setBackgroundResource(R.drawable.ic_answer_right)
                            mBinding.txtYourAnswer.hide()
                            mBinding.txtAnswer.show()
                            mBinding.txtAnswer.text = context.getText(R.string.correctAnswer).toString() + " : " + data.userAnswer
                        } else {
                            mBinding.img.setBackgroundResource(R.drawable.ic_answer_wrong)
                            mBinding.txtAnswer.show()
                            mBinding.txtYourAnswer.show()
                            mBinding.txtAnswer.text = context.getText(R.string.correctAnswer).toString() + " : " + correctAns
                            mBinding.txtYourAnswer.text = context.getText(R.string.YourAnswer).toString() + " : " + data.userAnswer
                        }
                    }

                    if (data.type == BeginnerExamQuestionType.Count){

                        CoroutineScope(Dispatchers.IO).launch {
                            AbacusUtils.setAbacusColumnTheme(examAbacusTheme,AbacusBeadType.ExamResult,mBinding.layoutAbacus1.abacusTop,mBinding.layoutAbacus1.abacusBottom,mBinding.layoutAbacus2.abacusTop,mBinding.layoutAbacus2.abacusBottom)
                        }
                        mBinding.layoutAbacus1.relAbacus.show()
                        mBinding.layoutAbacus2.relAbacus.hide()
                        mBinding.layoutAbacus2.abacusTop.hide()
                        mBinding.layoutAbacus2.abacusBottom.hide()
                        mBinding.layoutAbacus1.abacusTop.show()
                        mBinding.layoutAbacus1.abacusBottom.show()
                        mBinding.imgSign1.hide()

                        CoroutineScope(Dispatchers.IO).launch {
                            delay(500)
                            AbacusUtils.setNumber(data.value,mBinding.layoutAbacus1.abacusTop,mBinding.layoutAbacus1.abacusBottom)
                        }
                    }else{
                        val list1ImageCount = data.value.toInt()
                        val list2ImageCount = data.value2.toInt()
                        CoroutineScope(Dispatchers.IO).launch {
                            AbacusUtils.setAbacusColumnTheme(examAbacusTheme,AbacusBeadType.ExamResult,mBinding.layoutAbacus1.abacusTop,mBinding.layoutAbacus1.abacusBottom,mBinding.layoutAbacus2.abacusTop,mBinding.layoutAbacus2.abacusBottom)
                        }
                        mBinding.imgSign1.show()
                        mBinding.layoutAbacus2.relAbacus.show()
                        mBinding.layoutAbacus1.relAbacus.show()
                        mBinding.layoutAbacus2.abacusTop.show()
                        mBinding.layoutAbacus2.abacusBottom.show()
                        mBinding.layoutAbacus1.abacusTop.show()
                        mBinding.layoutAbacus1.abacusBottom.show()

                        if (data.type == BeginnerExamQuestionType.Additions){
                            mBinding.imgSign1.setImageResource(R.drawable.cal_plus)
                        }else if (data.type == BeginnerExamQuestionType.Subtractions){
                            mBinding.imgSign1.setImageResource(R.drawable.cal_minus)
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(500)
                            AbacusUtils.setNumber(list1ImageCount.toString(),mBinding.layoutAbacus1.abacusTop,mBinding.layoutAbacus1.abacusBottom,list2ImageCount.toString(),mBinding.layoutAbacus2.abacusTop,mBinding.layoutAbacus2.abacusBottom)
                        }
                    }
                }
            }
            1 ->{
                with(holder as FormViewHolder){
                    val context = holder.mBinding.root.context

                    if (data.userAnswer?.isEmpty() == true) {
                        mBinding.img.hide()
                        mBinding.txtYourAnswer.show()
                        mBinding.txtYourAnswer.text =
                            mBinding.txtYourAnswer.context.getText(R.string.SKipped)
                    } else {
                        mBinding.img.show()
                        val tempAns = when (data.type) {
                            BeginnerExamQuestionType.Additions -> {
                                data.value+"+"+data.value2
                            }
                            BeginnerExamQuestionType.Subtractions -> {
                                data.value+"-"+data.value2
                            }
                            else -> {
                                data.value
                            }
                        }
                        val resultObject = mCalculator.getResult(tempAns,tempAns)
                        val correctAns = CommonUtils.removeTrailingZero(resultObject)

                        if (correctAns.equals(data.userAnswer, ignoreCase = true)) {
                            mBinding.img.setBackgroundResource(R.drawable.ic_answer_right)
                            mBinding.txtYourAnswer.hide()
                            mBinding.txtAnswer.show()
                            mBinding.txtAnswer.text = context.getText(R.string.correctAnswer).toString() + " : " + data.userAnswer
                        } else {
                            mBinding.img.setBackgroundResource(R.drawable.ic_answer_wrong)
                            mBinding.txtAnswer.show()
                            mBinding.txtYourAnswer.show()
                            mBinding.txtAnswer.text = context.getText(R.string.correctAnswer).toString() + " : " + correctAns
                            mBinding.txtYourAnswer.text = context.getText(R.string.YourAnswer).toString() + " : " + data.userAnswer
                        }
                    }

                    mBinding.recyclerviewObjects2.hide()
                    mBinding.spaceBetween.hide()
                    mBinding.imgSign.hide()
                    if (data.type == BeginnerExamQuestionType.Count){
                        mBinding.linearQuestion.show()

                        var list1ImageCount = 0
                        var list2ImageCount = 0
                        val totalCount = data.value.toInt()
                        if (totalCount > 10){
                            list1ImageCount = totalCount/2
                            list2ImageCount = totalCount - list1ImageCount
                        }else if (totalCount < 6){
                            list1ImageCount = totalCount
                        }else{
                            list1ImageCount = 5
                            list2ImageCount = totalCount - 5
                        }
                        if (list1ImageCount > 0){
                            val objectListAdapter1= ObjectListResultAdapter(list1ImageCount, data.imageData)
                            mBinding.recyclerviewObjects1.layoutManager = GridLayoutManager(context,list1ImageCount)
                            mBinding.recyclerviewObjects1.adapter = objectListAdapter1
                        }
                        if (list2ImageCount > 0){
                            val objectListAdapter2= ObjectListResultAdapter(list2ImageCount, data.imageData)
                            mBinding.recyclerviewObjects2.layoutManager = GridLayoutManager(context,list2ImageCount)
                            mBinding.recyclerviewObjects2.adapter = objectListAdapter2
                            mBinding.recyclerviewObjects2.show()
                            mBinding.spaceBetween.show()
                        }
                    }else{
                        val list1ImageCount = data.value.toInt()
                        val list2ImageCount = data.value2.toInt()

                        if (data.type == BeginnerExamQuestionType.Additions){
                            mBinding.imgSign.setImageResource(R.drawable.cal_plus)
                        }else if (data.type == BeginnerExamQuestionType.Subtractions){
                            mBinding.imgSign.setImageResource(R.drawable.cal_minus)
                        }

                        val objectListAdapter1= ObjectListResultAdapter(list1ImageCount, data.imageData)
                        mBinding.recyclerviewObjects1.layoutManager = GridLayoutManager(context,list1ImageCount)
                        mBinding.recyclerviewObjects1.adapter = objectListAdapter1

                        val objectListAdapter2= ObjectListResultAdapter(list2ImageCount, data.imageData)
                        mBinding.recyclerviewObjects2.layoutManager = GridLayoutManager(context,list2ImageCount)
                        mBinding.recyclerviewObjects2.adapter = objectListAdapter2

                        mBinding.recyclerviewObjects2.show()
                        mBinding.imgSign.show()
                    }
                }
            }
        }
    }

    class FormViewHolder(itemBinding: RawExamResultLevel1Binding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var mBinding: RawExamResultLevel1Binding = itemBinding
    }

    class ViewHolderAbacus(itemBinding: RawExamResultLevel1AbacusBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var mBinding: RawExamResultLevel1AbacusBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}