package com.jigar.me.ui.view.dashboard.fragments.exam.result

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.local.data.BeginnerExamPaper
import com.jigar.me.data.local.data.BeginnerExamQuestionType
import com.jigar.me.databinding.RawExamResultLevel1Binding
import com.jigar.me.utils.Calculator
import com.jigar.me.utils.CommonUtils
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.show

class ExamResultLevel1Adapter(
    private var listData: List<BeginnerExamPaper>
) :
    RecyclerView.Adapter<ExamResultLevel1Adapter.FormViewHolder>() {

    private lateinit var mCalculator: Calculator
    fun setData(listData: List<BeginnerExamPaper>) {
        this.listData = listData
        mCalculator = Calculator()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FormViewHolder {
        val binding = RawExamResultLevel1Binding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data = listData[position]
        val context = holder.binding.root.context
        holder.binding.recyclerviewObjects2.hide()
        holder.binding.spaceBetween.hide()
        holder.binding.imgSign.hide()
        if (data.type == BeginnerExamQuestionType.Count){
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
                holder.binding.recyclerviewObjects1.layoutManager = GridLayoutManager(context,list1ImageCount)
                holder.binding.recyclerviewObjects1.adapter = objectListAdapter1
            }
            if (list2ImageCount > 0){
                val objectListAdapter2= ObjectListResultAdapter(list2ImageCount, data.imageData)
                holder.binding.recyclerviewObjects2.layoutManager = GridLayoutManager(context,list2ImageCount)
                holder.binding.recyclerviewObjects2.adapter = objectListAdapter2
                holder.binding.recyclerviewObjects2.show()
                holder.binding.spaceBetween.show()
            }


        }else if (data.type == BeginnerExamQuestionType.Additions || data.type == BeginnerExamQuestionType.Subtractions){
            if (data.type == BeginnerExamQuestionType.Additions){
                holder.binding.imgSign.setImageResource(R.drawable.cal_plus)
            }else if (data.type == BeginnerExamQuestionType.Subtractions){
                holder.binding.imgSign.setImageResource(R.drawable.cal_minus)
            }
            val list1ImageCount = data.value.toInt()
            val list2ImageCount = data.value2.toInt()

            val objectListAdapter1= ObjectListResultAdapter(list1ImageCount, data.imageData)
            holder.binding.recyclerviewObjects1.layoutManager = GridLayoutManager(context,list1ImageCount)
            holder.binding.recyclerviewObjects1.adapter = objectListAdapter1

            val objectListAdapter2= ObjectListResultAdapter(list2ImageCount, data.imageData)
            holder.binding.recyclerviewObjects2.layoutManager = GridLayoutManager(context,list2ImageCount)
            holder.binding.recyclerviewObjects2.adapter = objectListAdapter2

            holder.binding.recyclerviewObjects2.show()
            holder.binding.imgSign.show()

        }

        if (data.userAnswer?.isEmpty() == true) {
            holder.binding.img.hide()
            holder.binding.txtYourAnswer.show()
            holder.binding.txtYourAnswer.text =
                holder.binding.txtYourAnswer.context.getText(R.string.SKipped)
        } else {
            holder.binding.img.show()
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
                holder.binding.img.setBackgroundResource(R.drawable.ic_answer_right)
                holder.binding.txtYourAnswer.hide()
                holder.binding.txtAnswer.show()
                holder.binding.txtAnswer.text = context.getText(R.string.correctAnswer).toString() + " : " + data.userAnswer
            } else {
                holder.binding.img.setBackgroundResource(R.drawable.ic_answer_wrong)
                holder.binding.txtAnswer.show()
                holder.binding.txtYourAnswer.show()
                holder.binding.txtAnswer.text = context.getText(R.string.correctAnswer).toString() + " : " + correctAns
                holder.binding.txtYourAnswer.text = context.getText(R.string.YourAnswer).toString() + " : " + data.userAnswer
            }
        }

    }

    class FormViewHolder(itemBinding: RawExamResultLevel1Binding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RawExamResultLevel1Binding = itemBinding
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}