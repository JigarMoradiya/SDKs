package com.jigar.me.ui.view.dashboard.fragments.sudoku.history.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.databinding.RawSudokuHistoryListBinding
import com.jigar.me.ui.viewmodel.SudokuViewModel
import com.jigar.me.utils.DateTimeUtils
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.show
import com.jigar.me.utils.sudoku.SudukoConst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SudokuHistoryListAdapter(
    val listener: ListClicks,
    var mListData: List<SudukoPlay>,
    val dataManager: SudokuViewModel
) :
    RecyclerView.Adapter<SudokuHistoryListAdapter.ViewHolder>() {
    interface ListClicks {
        fun itemClick(position: Int, type: String)
    }

    override fun onBindViewHolder(p0: ViewHolder, position: Int) {
        with(p0){
            val context = binding?.root?.context
            binding?.txtTime?.text = HtmlCompat.fromHtml("Start at : <b>"+ DateTimeUtils.getDateString(mListData[position].addedOn, DateTimeUtils.ddMMMyyyyhhmma)+"</b>",HtmlCompat.FROM_HTML_MODE_COMPACT)
            if (mListData[position].level == SudukoConst.Level_4By4 || mListData[position].level == SudukoConst.Level_6By6){
                binding?.txtTitle?.text = mListData[position].level + " Sudoku"
            }else{
                binding?.txtTitle?.text = mListData[position].level + " Level"
            }

            CoroutineScope(Dispatchers.Default).launch {
                val sudokuLevelList = dataManager.getRoomID_SudokuLevel1(mListData[position].roomID)
                CoroutineScope(Dispatchers.Main).launch {
                    if (sudokuLevelList.isNotEmpty()) {
                        val timer: Int = sudokuLevelList[0].playTime.toInt()
                        val hours = timer / 3600
                        val minutes = timer % 3600 / 60
                        val seconds = timer % 60
                        var totalTime = ""
                        if (hours > 0){
                            totalTime = "$hours h "
                        }
                        if (minutes > 0){
                            totalTime = "$totalTime$minutes m "
                        }
                        if (seconds > 0){
                            totalTime = "$totalTime$seconds s"
                        }
                        binding?.txtStatus?.text = totalTime

                        CoroutineScope(Dispatchers.Default).launch {
                            val isWrongAnswerEmpty = dataManager.getRoomID_SudokuAnswer1(mListData[position].roomID).isEmpty()
                            CoroutineScope(Dispatchers.Main).launch {
                                if (sudokuLevelList[0].status == "1" && isWrongAnswerEmpty) {
                                    context?.let { binding?.txtStatus?.setTextColor(ContextCompat.getColor(it,R.color.colorPrimary)) }
                                    binding?.btnCompleted?.show()
                                    binding?.btnContinue?.hide()
                                } else {
                                    context?.let { binding?.txtStatus?.setTextColor(ContextCompat.getColor(it,R.color.grey_600)) }
                                    binding?.btnCompleted?.hide()
                                    binding?.btnContinue?.show()
                                }
                            }
                        }
                    } else {
                        context?.let { binding?.txtStatus?.setTextColor(ContextCompat.getColor(it,R.color.grey_600)) }
                        binding?.btnCompleted?.hide()
                        binding?.btnContinue?.show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = p0.context.layoutInflater.inflate(R.layout.raw_sudoku_history_list, p0, false)
        return ViewHolder(v,listener)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    fun setList(data: List<SudukoPlay>) {
        mListData = data
        notifyItemRangeChanged(0,mListData.size)
    }

    class ViewHolder(itemView: View, listener: ListClicks) : RecyclerView.ViewHolder(itemView) {
        var binding: RawSudokuHistoryListBinding? = DataBindingUtil.bind(itemView)
        init {
            binding?.btnCompleted?.onClick {
                listener.itemClick(layoutPosition,"")
            }
            binding?.btnContinue?.onClick {
                listener.itemClick(layoutPosition,"")
            }
        }
    }
}