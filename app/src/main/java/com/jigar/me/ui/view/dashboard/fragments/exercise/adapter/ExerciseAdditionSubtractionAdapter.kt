package com.jigar.me.ui.view.dashboard.fragments.exercise.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.databinding.RowExerciseQuestionLayoutBinding
import com.jigar.me.utils.extensions.*

class ExerciseAdditionSubtractionAdapter(
    private var questions: List<String>
) : RecyclerView.Adapter<ExerciseAdditionSubtractionAdapter.FormViewHolder>() {
    var currentStep = 0
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FormViewHolder {
        val binding = RowExerciseQuestionLayoutBinding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data = questions[position]
        with(holder.binding){
            val context = root.context
            val que = data.replace("+","").replace("-","")
            if (que.length > 4){
                if (currentStep == position){
                    tvQuestion1.setTextColor(ContextCompat.getColor(context,R.color.pink_400))
                    imgSymbol.setColorFilter(ContextCompat.getColor(context,R.color.pink_400))
                }else{
                    tvQuestion1.setTextColor(ContextCompat.getColor(context,R.color.black))
                    imgSymbol.setColorFilter(ContextCompat.getColor(context,R.color.black))
                }
                tvQuestion1.text = que
                tvQuestion1.show()
                tvQuestion.hide()
            }else{
                if (currentStep == position){
                    tvQuestion.setTextColor(ContextCompat.getColor(context,R.color.pink_400))
                    imgSymbol.setColorFilter(ContextCompat.getColor(context,R.color.pink_400))
                }else{
                    tvQuestion.setTextColor(ContextCompat.getColor(context,R.color.black))
                    imgSymbol.setColorFilter(ContextCompat.getColor(context,R.color.black))
                }
                tvQuestion.text = que
                tvQuestion1.hide()
                tvQuestion.show()
            }

            if (data.contains("-")){
                imgSymbol.show()
            }else{
                imgSymbol.invisible()
            }
            root.onClick {
                if (currentStep != questions.lastIndex){
                    val previousPos = currentStep
                    currentStep++
                    notifyItemChanged(currentStep)
                    notifyItemChanged(previousPos)
                }
            }
        }

    }

    class FormViewHolder(
        itemBinding: RowExerciseQuestionLayoutBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RowExerciseQuestionLayoutBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return questions.size
    }
}