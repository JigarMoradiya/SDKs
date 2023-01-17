package com.jigar.me.ui.view.dashboard.fragments.abacus.half.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jigar.me.R
import com.jigar.me.databinding.RowQuestionLayoutBinding
import com.jigar.me.utils.Constants
import com.jigar.me.utils.ViewUtils
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.invisible
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.show
import java.util.*

class AbacusDivisionTypeAdapter(
    private var abecuseItems: List<HashMap<String, String>>,private var isStepByStep: Boolean
) :
    RecyclerView.Adapter<AbacusDivisionTypeAdapter.FormViewHolder>() {

    private val highlightDetail = HashMap<Int, Pair<Int, Int>>()
    private var isClear = false
    private var isLastTemp = false
    private var nextDivider: Long = 0
    private  var nextDividerIteration:Long = 0
    private var totalRequiredIteration = 0
    private var initialDividerPosition = 2
    private var nextDivider1: Long = 0
    var nextHighlightedPosition = 0
    var currentTablePosition = 0

    fun setData(listData: List<HashMap<String, String>>, isStepByStep: Boolean) {
        this.abecuseItems = listData
        this.isStepByStep = isStepByStep
        isLastTemp = false
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
        holder.binding.tvSymbol.text = data[Constants.Sign] // intentionally added space before symbol

        holder.binding.tvQuestion.text = data[Constants.Que]
        if (holder.binding.tvSymbol.text.toString().isEmpty()) {
            holder.binding.tvSymbol.invisible()
        } else {
            holder.binding.tvSymbol.show()
        }
        if (isStepByStep) {
            val highlightPOs = highlightDetail[position]
            if (highlightPOs != null) {
                if (!isClear) {
                    holder.binding.tvSymbol.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    if (!TextUtils.isEmpty(data[Constants.Que])) {
                        holder.binding.tvQuestion.text = span(
                            data[Constants.Que],
                            highlightPOs.first!!, highlightPOs.second!!,context
                        )
                    }
                } else {
                    holder.binding.tvSymbol.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    holder.binding.tvQuestion.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                }
            }
            if (position == 1 && abecuseItems.size > 2) {
                holder.binding.ivDivider.show()
            } else {
                holder.binding.ivDivider.hide()
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

    fun setDefaultHighlight() {
        if (abecuseItems.size >= 2) {
            val first = abecuseItems[0][Constants.Que]
            val second = java.lang.Float.valueOf(abecuseItems[1][Constants.Que])
            calculateHiglightCount(first, second.toInt(), 1, 1, 0)

            val pair = Pair<Int, Int>(0, nextHighlightedPosition)
            highlightDetail[0] = pair
            val pair1 = Pair(0, abecuseItems[1][Constants.Que]!!.length)
            highlightDetail[1] = pair1
            highlightDetail[2] = Pair(0, 0)
            highlightDetail[3] = Pair(0, 0)

        }
    }

    fun isLastStep(): Boolean {
        return if (!isLastTemp) {
            if (highlightDetail.size > 0) {
                highlightDetail[0]!!.second == abecuseItems[0][Constants.Que]!!.length
            } else false
        } else {
            true
        }
    }


    fun getCurrentStep(): Int? {
        return highlightDetail[0]!!.second
    }
    fun clearIterationCount() {
        totalRequiredIteration = 0
    }

    private fun span(text: String?, startPosition: Int, endPosition: Int, context: Context): Spannable {
        if (!TextUtils.isEmpty(text) && startPosition <= text!!.length && endPosition <= text.length) {
            val wordtoSpan = SpannableString(text)
            wordtoSpan.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.black_text)),
                0,
                text.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            wordtoSpan.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.red)
                ), startPosition, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            return wordtoSpan
        }
        return SpannableString("")
    }
    fun reset() {
        for (i in 2 until abecuseItems.size) {
            abecuseItems[i][Constants.Que] = ""
            abecuseItems[i][Constants.Sign] = ""
            abecuseItems[i][Constants.Hint] = ""
        }
        nextDivider = 0L
        initialDividerPosition = 2
        setDefaultHighlight()
        notifyDataSetChanged()
    }

    fun goToNextStep() {
        try {
            val data = abecuseItems[0]
            /*Long currentSum = getCurrentSumVal();*/
            /*shift 0 position*/
            val firstEndPostion = highlightDetail[0]!!.second!!
            if (firstEndPostion < data[Constants.Que]!!.length) {
                calculateHiglightCount(
                    abecuseItems[0][Constants.Que], abecuseItems[1][Constants.Que]!!
                        .toInt(),
                    1, 1, firstEndPostion
                )
                if (firstEndPostion == nextHighlightedPosition) {
//                    current value is 0 so add +1 to position highlight and move next
                    val pair = Pair<Int, Int>(firstEndPostion, nextHighlightedPosition + 1)
                    highlightDetail[0] = pair
                    goToNextStep()
                    return
                }
                if (firstEndPostion > nextHighlightedPosition && data[Constants.Que]!!.endsWith("0")){
                    isLastTemp = true
                    return
                }

                val pair = Pair<Int, Int>(firstEndPostion, nextHighlightedPosition)
                highlightDetail[0] = pair
                if (abecuseItems.size > initialDividerPosition) {
                    if (initialDividerPosition > 2) {
                        /*unhighlight previous divider*/
                        val pairInternal = Pair(0, 0)
                        highlightDetail[initialDividerPosition - 1] = pairInternal
                    }
                    val abecuseItem = abecuseItems[initialDividerPosition]
                    abecuseItem[Constants.Que] = nextDivider.toString()
                    val pairInternal = Pair(0, abecuseItem[Constants.Que]!!.length)
                    highlightDetail[initialDividerPosition] = pairInternal
                    initialDividerPosition++
                } else {
                    /*unhighlight previous divider*/
                    val pairInternal = Pair(0, 0)
                    highlightDetail[initialDividerPosition - 1] = pairInternal
                }
            } else {
                val pairInternal = Pair(0, 0)
                highlightDetail[initialDividerPosition - 1] = pairInternal
                //                sum completed
                return
            }

            /*Long nextSumVal = getCurrentSumVal();
            if (nextSumVal.equals(currentSum)) {
                goToNextStep();
            }*/notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getCurrentSumVal(): Long? {
        if (abecuseItems.size >= 2 && highlightDetail.size > 0) {
            val firstItem = abecuseItems[0]
            val secondItem = abecuseItems[1]
            val firstPair = highlightDetail[0]
            /*first step*/
            val divideBy = Integer.valueOf(secondItem[Constants.Que])
            val fullQuestion = firstItem[Constants.Que]
            currentTablePosition = 0

            /*first step completed
         * now need to take highlight end position from question 1 and need perform same step as above */
            return calculateCurrentSumVal(
                fullQuestion,
                divideBy,
                firstPair!!.second,
                1
            )
        }
        return null
    }
    fun calculateCurrentSumVal(
        fullQuestion: String?,
        divideBy: Int,
        length: Int?,
        currentRecursionCount: Int
    ): Long? {
        var currentsumval = 0L
        //        if (currentRecursionCount >= length) {
//            return currentsumval;
//        }
        for (i in 0..length!!) {
            if (i > fullQuestion!!.length) {
                break
            }
            val que = fullQuestion.substring(0, i)
            if (!TextUtils.isEmpty(que)) {
                val queInt = Integer.valueOf(que)
                if (queInt >= divideBy) {
//                    if (((i + 1) <= fullQuestion.length()) && fullQuestion.substring(0, i + 1).endsWith("0")) {
//                        continue;
//                    }
                    var j = 1
                    while (true) {
                        if (queInt - divideBy * j < divideBy) {
                            break
                        }
                        j++
                    }
                    var curSum = j.toString() + ""
                    for (k in i until fullQuestion.length) {
                        curSum += "0"
                    }
                    currentsumval = java.lang.Long.valueOf(curSum)
                    nextDivider = java.lang.Long.valueOf(fullQuestion) - currentsumval * divideBy
                    if (currentRecursionCount < length && i < length) {
                        var nextQue = nextDivider.toString()
                        curSum = ""
                        //                        String nextQue = String.valueOf(nextDivider1);
                        for (k in 0 until fullQuestion.length - nextQue.length) {
                            curSum += "0"
                        }
                        nextQue =
                            curSum + nextQue // to maintain lenth = full question length we added 0
                        currentsumval += calculateCurrentSumVal(
                            nextQue,
                            divideBy,
                            length,
                            currentRecursionCount + 1
                        )!!
                    }
                    return currentsumval
                }
            }
        }
        return 0L
    }

    fun calculateHiglightCount(
        fullQuestion: String?,
        divideBy: Int,
        position: Int?,
        currentRecursionCount: Int,
        startLength: Int
    )
    {
        if (currentRecursionCount == 1) {
            nextHighlightedPosition = 0
        }
        var currentsumval = 0L

//        if (currentRecursionCount >= fullQuestion!!.length) {
//            return currentsumval;
//        }
//        for (int i = 0; i <= length; i++) {
//        if (position > fullQuestion.length()) {
//            return;
//        }
        nextHighlightedPosition++
        val que = fullQuestion!!.substring(0, position!!)
        if (!TextUtils.isEmpty(que)) {
            val queInt = Integer.valueOf(que)
            if (queInt >= divideBy) {
//                if (((position) <= fullQuestion.length) && fullQuestion.substring(0, position).endsWith("0")) {
//                    return
//                }
                var j = 1
                while (true) {
                    if (queInt - divideBy * j < divideBy) {
                        break
                    }
                    j++
                }
                currentTablePosition = j
                var curSum = j.toString() + ""
                for (k in position until fullQuestion.length) {
                    curSum += "0"
                }
                currentsumval = java.lang.Long.valueOf(curSum)
                nextDivider1 = java.lang.Long.valueOf(fullQuestion) - currentsumval * divideBy
                if (nextDivider1 != 0L && position <= startLength) {
                    curSum = ""
                    var nextQue = nextDivider1.toString()
                    for (k in 0 until fullQuestion.length - nextQue.length) {
                        curSum += "0"
                    }
                    nextQue =
                        curSum + nextQue // to maintain lenth = full question length we added 0
                    calculateHiglightCount(
                        nextQue,
                        divideBy,
                        position + 1,
                        currentRecursionCount + 1,
                        startLength
                    )
                }
            } else {
                calculateHiglightCount(
                    fullQuestion,
                    divideBy,
                    position + 1,
                    currentRecursionCount + 1,
                    startLength
                )
            }
        }

    }
    fun getDivideIterationCount(fullQuestion: String, divideBy: Int): Long {
        var currentsumval = 0L
        for (i in 0..fullQuestion.length) {
            val que = fullQuestion.substring(0, i)
            if (!TextUtils.isEmpty(que)) {
                val queInt = Integer.valueOf(que)
                if (queInt >= divideBy) {
//                    if (((i + 1) <= fullQuestion.length()) && fullQuestion.substring(0, i + 1).endsWith("0")) {
//                        continue;
//                    }
                    var j = 1
                    while (true) {
                        if (queInt - divideBy * j < divideBy) {
                            break
                        }
                        j++
                    }
                    var curSum = j.toString() + ""
                    for (k in i until fullQuestion.length) {
                        curSum += "0"
                    }
                    currentsumval = java.lang.Long.valueOf(curSum)
                    nextDividerIteration =
                        java.lang.Long.valueOf(fullQuestion) - currentsumval * divideBy
                    totalRequiredIteration++
                    if (nextDividerIteration != 0L) {
                        val nextQue = nextDividerIteration.toString()
                        currentsumval += getDivideIterationCount(nextQue, divideBy)
                    }
                    return currentsumval
                }
            }
        }
        return 0L
    }

    fun getNextDivider(): Long {
        return nextDivider
    }
    fun getFinalSumVal(): Double? {

        /*divide have 2 items. other 2 items used for calculation purpose*/
        if (abecuseItems.isNotEmpty()) {
            var expression = ""
            for (i in 0..1) {
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

    fun getTotalRequiredIteration(): Int {
        return totalRequiredIteration
    }
}