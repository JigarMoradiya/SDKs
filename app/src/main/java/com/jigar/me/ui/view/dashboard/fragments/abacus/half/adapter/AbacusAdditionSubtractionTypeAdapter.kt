package com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.databinding.RawPurchaseBinding
import com.jigar.me.databinding.RowQuestionLayoutBinding
import com.jigar.me.utils.Constants
import com.jigar.me.utils.ViewUtils
import com.jigar.me.utils.extensions.invisible
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.show
import java.util.*

class AbacusAdditionSubtractionTypeAdapter(
    private var abecuseItems: List<HashMap<String, String>>,
    private val mListener: HintListener,private var isStepByStep: Boolean
) :
    RecyclerView.Adapter<AbacusAdditionSubtractionTypeAdapter.FormViewHolder>() {
    interface HintListener {
        fun onCheckHint(hint: String?, que: String?, Sign: String?)
    }

    private var currentStep = 0

    fun setData(listData: List<HashMap<String, String>>,isStepByStep : Boolean) {
        this.abecuseItems = listData
        this.isStepByStep = isStepByStep
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FormViewHolder {
        val binding = RowQuestionLayoutBinding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data = abecuseItems[position]
        val context = holder.binding.tvQuestion.context

        holder.binding.tvSymbol.text = data[Constants.Sign]
        holder.binding.tvQuestion.text = data[Constants.Que]
        if (holder.binding.tvSymbol.text.toString().isEmpty()) {
            holder.binding.tvSymbol.invisible()
        } else {
            holder.binding.tvSymbol.show()
        }
        if (isStepByStep) {
            if (currentStep == position) {
                holder.binding.tvSymbol.setTextColor(
                    ContextCompat.getColor(context, R.color.red)
                )
                holder.binding.tvQuestion.setTextColor(
                    ContextCompat.getColor(context, R.color.red)
                )
            } else {
                holder.binding.tvSymbol.setTextColor(
                    ContextCompat.getColor(context, R.color.black_text)
                )
                holder.binding.tvQuestion.setTextColor(
                    ContextCompat.getColor(context, R.color.black_text)
                )
            }
        }
    }

    class FormViewHolder(
        itemBinding: RowQuestionLayoutBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RowQuestionLayoutBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return abecuseItems.size
    }

    fun getCurrentStep(): Int {
        return currentStep
    }

    fun reset() {
        currentStep = 0
        notifyDataSetChanged()
    }

    fun goToNextStep() {
        this.currentStep++
        if (currentStep < abecuseItems.size) {
            val data = abecuseItems[currentStep]
            if (data[Constants.Que]!!.trim { it <= ' ' } == "0") {
                goToNextStep()
            } else {
                mListener.onCheckHint(
                    data[Constants.Hint],
                    data[Constants.Que], data[Constants.Sign]
                )
            }
        }
        notifyDataSetChanged()
    }

    fun getCurrentSumVal(): Double? {
        if (abecuseItems.size > currentStep) {
            var expression = ""
            for (i in 0..currentStep) {
                val data = abecuseItems[i]
                expression += data[Constants.Sign]!!.trim { it <= ' ' } + data[Constants.Que]!!.trim { it <= ' ' }
            }
            return ViewUtils.calculateStringExpression(expression)
        }
        return null
    }

    fun getFinalSumVal(): Double? {
        if (abecuseItems.isNotEmpty()) {
            var expression = ""
            for (i in abecuseItems.indices) {
                val data = abecuseItems[i]
                expression += data[Constants.Sign]!!.trim { it <= ' ' } + data[Constants.Que]!!.trim { it <= ' ' }
            }
            return ViewUtils.calculateStringExpression(expression)
        }
        return null
    }

}