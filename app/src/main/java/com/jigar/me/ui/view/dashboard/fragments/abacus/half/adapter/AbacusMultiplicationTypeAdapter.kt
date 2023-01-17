package com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.databinding.RowQuestionLayoutBinding
import com.jigar.me.utils.Constants
import com.jigar.me.utils.ViewUtils
import com.jigar.me.utils.extensions.invisible
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.show
import java.util.*

class AbacusMultiplicationTypeAdapter(
    private var abecuseItems: List<HashMap<String, String>>,private var isStepByStep: Boolean
) :
    RecyclerView.Adapter<AbacusMultiplicationTypeAdapter.FormViewHolder>() {

    private val highlightDetail = HashMap<Int, Int>()
    private var isClear = false
    private var currentStep = 0

    fun setData(listData: List<HashMap<String, String>>, isStepByStep: Boolean) {
        this.abecuseItems = listData
        this.isStepByStep = isStepByStep

        highlightDetail[0] = 0
        highlightDetail[1] = 0
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
        holder.binding.tvSymbol.text =
            Objects.requireNonNull<String>(data[Constants.Sign]).replace("*", "x")
        holder.binding.tvQuestion.text = data[Constants.Que]
        if (holder.binding.tvSymbol.text.toString().isEmpty()) {
            holder.binding.tvSymbol.invisible()
        } else {
            holder.binding.tvSymbol.show()
        }
        if (isStepByStep) {
            val highlightPOs = highlightDetail[position]!!
            if (!isClear) {
                holder.binding.tvSymbol.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                holder.binding.tvQuestion.text = span(
                    data[Constants.Que],
                    highlightPOs, context
                )
            } else {
                holder.binding.tvSymbol.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                holder.binding.tvQuestion.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            }
        }
    }

    class FormViewHolder(
        itemBinding: RowQuestionLayoutBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RowQuestionLayoutBinding = itemBinding

        init {
        }
    }

    override fun getItemCount(): Int {
        return abecuseItems.size
    }

    fun getTable(context: Context): SpannableString? {
        if (highlightDetail.size > 1) {
            val position = highlightDetail[1]!!
            val position0 = highlightDetail[0]!!
            val abecuseItem = abecuseItems[1]
            val abecuseItem0 = abecuseItems[0]
            return ViewUtils.getTable(
                context,
                Integer.valueOf(
                    abecuseItem[Constants.Que]!!.substring(position, position + 1)
                ),
                Integer.valueOf(abecuseItem0[Constants.Que]!!.substring(position0, position0 + 1))
            )
        }
        return null
    }

    private fun span(text: String?, position: Int, context: Context): Spannable {
        val wordtoSpan = SpannableString(text)
        wordtoSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.black_text)),
            0,
            text!!.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        wordtoSpan.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.red)
            ), position, position + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return wordtoSpan
    }

    fun getCurrentStep(): HashMap<Int, Int> {
        return highlightDetail
    }

    fun getItem(item: Int): HashMap<String, String> {
        return abecuseItems[item]
    }

    fun reset() {
        isClear = false
        highlightDetail[0] = 0
        highlightDetail[1] = 0
        notifyDataSetChanged()
    }

    fun goToNextStep() {
        try {
            val firstItem = abecuseItems[0]
            val secondItem = abecuseItems[1]
            val currentSum = getCurrentSumVal()
            if (highlightDetail[0]!! < firstItem[Constants.Que]!!.length - 1) {
                highlightDetail[0] = highlightDetail[0]!! + 1
            } else if (highlightDetail[1]!! < secondItem[Constants.Que]!!.length - 1) {
                highlightDetail[1] = highlightDetail[1]!! + 1
                highlightDetail[0] = 0
            } else {
//                sum completed
                return
            }
            val nextSumVal = getCurrentSumVal()
            if (nextSumVal == currentSum) {
                goToNextStep()
            }
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun getCurrentSumVal(): Double? {
        if (abecuseItems.size >= 2) {
            val firstItem = abecuseItems[0]
            val secondItem = abecuseItems[1]
            var currentsumval = 0.0
            for (i in 0..highlightDetail[1]!!) {
                for (j in 0..(if (i == highlightDetail[1]) highlightDetail[0] else firstItem[Constants.Que]!!.length - 1)!!) {
                    var firstVal = firstItem[Constants.Que]!![j].toString()
                    for (k in j + 1 until firstItem[Constants.Que]!!.length) {
                        firstVal += "0"
                    }
                    var secondVal = secondItem[Constants.Que]!![i].toString()
                    for (k in i + 1 until secondItem[Constants.Que]!!.length) {
                        secondVal += "0"
                    }
                    val expression = "$firstVal*$secondVal"
                    currentsumval += ViewUtils.calculateStringExpression(expression)
                }
            }
            return currentsumval
        }
        return null
    }

    fun getFinalSumVal(): Double? {
        if (abecuseItems.size > 0) {
            var expression = ""
            for (i in abecuseItems.indices) {
                val abecuseItem = abecuseItems[i]
                expression += abecuseItem[Constants.Sign]!!.trim { it <= ' ' } + abecuseItem[Constants.Que]!!
                    .trim { it <= ' ' }
            }
            return ViewUtils.calculateStringExpression(expression)
        }
        return null
    }

    fun clearHighlight() {
        isClear = true
        notifyDataSetChanged()
    }
}