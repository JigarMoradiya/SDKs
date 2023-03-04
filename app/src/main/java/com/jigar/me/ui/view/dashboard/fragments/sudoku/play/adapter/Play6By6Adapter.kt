package com.jigar.me.ui.view.dashboard.fragments.sudoku.play.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.R
import com.jigar.me.data.model.dbtable.suduko.SudukoAnswerStatus
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.databinding.RawLayoutSudokuPlay6by6Binding
import com.jigar.me.ui.viewmodel.SudokuViewModel
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.show
import com.jigar.me.utils.sudoku.SudukoConst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Play6By6Adapter(
    var con: Context, mListData: ArrayList<String>?, roomId: String,
    val dataManager: SudokuViewModel, var prefManager : AppPreferencesHelper,
    val listener: ListClicks
) : RecyclerView.Adapter<Play6By6Adapter.MyViewHolder>() {

    interface ListClicks {
        fun clickOnBox()
    }

    var mListData: List<String>?
    private val roomId: String
    var selectedBoxPosition: Int = -1
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.raw_layout_sudoku_play_6by6,
            viewGroup, false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        SetValue(holder.binding.txt0,position,holder.binding.txt000,holder.binding.txtbg0,holder.binding.recyclerView0)
        SetValue(holder.binding.txt1,position,holder.binding.txt111,holder.binding.txtbg1,holder.binding.recyclerView1)
        SetValue(holder.binding.txt2,position,holder.binding.txt222,holder.binding.txtbg2,holder.binding.recyclerView2)
        SetValue(holder.binding.txt3,position,holder.binding.txt333,holder.binding.txtbg3,holder.binding.recyclerView3)
        SetValue(holder.binding.txt4,position,holder.binding.txt444,holder.binding.txtbg4,holder.binding.recyclerView4)
        SetValue(holder.binding.txt5,position,holder.binding.txt555,holder.binding.txtbg5,holder.binding.recyclerView5)
        if (prefManager.getCustomParam(SudukoConst.Notes, "0").equals("1", ignoreCase = true)) {
            setValueNotes(holder.binding.txt0,position,holder.binding.txt000,holder.binding.txtbg0,holder.binding.recyclerView0)
            setValueNotes(holder.binding.txt1,position,holder.binding.txt111,holder.binding.txtbg1,holder.binding.recyclerView1)
            setValueNotes(holder.binding.txt2,position,holder.binding.txt222,holder.binding.txtbg2,holder.binding.recyclerView2)
            setValueNotes(holder.binding.txt3,position,holder.binding.txt333,holder.binding.txtbg3,holder.binding.recyclerView3)
            setValueNotes(holder.binding.txt4,position,holder.binding.txt444,holder.binding.txtbg4,holder.binding.recyclerView4)
            setValueNotes(holder.binding.txt5,position,holder.binding.txt555,holder.binding.txtbg5,holder.binding.recyclerView5)
        }
    }

    override fun getItemCount(): Int {
        return if (mListData.isNullOrEmpty()) 0 else mListData!!.size
    }

    inner class MyViewHolder(viewLayout: View?) :
        RecyclerView.ViewHolder(viewLayout!!), View.OnClickListener {
        val binding: RawLayoutSudokuPlay6by6Binding = DataBindingUtil.bind(itemView)!!
        override fun onClick(v: View) {}

        init {
            itemView.setOnClickListener(this)
        }
    }

    private fun setValueNotes(
        txt: AppCompatTextView,
        position: Int,
        txt_pos: AppCompatTextView,
        txt_bg: AppCompatTextView,
        recyclerView: RecyclerView
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val str = txt.tag as String
            val temp_sudoku: List<SudukoPlay> =
                dataManager.getRoomID_CellPosition_SudokuPlay(roomId, position.toString() + str)
            CoroutineScope(Dispatchers.Main).launch {
                if (temp_sudoku.isNotEmpty()) {
                    var setvalue = ""
                    if (temp_sudoku[0].cellValue.isNotEmpty()) {
                        try {
                            setvalue = (temp_sudoku[0].cellValue.toInt() + 1).toString() + ""
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                    }
                    txt.text = setvalue
                    if (temp_sudoku[0].defaultSet.equals("Y",true)) {
                        txt.setBackgroundColor(ContextCompat.getColor(con,R.color.main_bg_not_empty))
                        txt.setTextColor(ContextCompat.getColor(con,R.color.text_color_not_empty))
                        txt.textSize = 22f
                        txt.show()
                        recyclerView.hide()
                        txt_pos.hide()
                        txt_bg.hide()
                    } else {
                        if (temp_sudoku[0].notes.isNotEmpty()) {
                            if (prefManager.getCustomParam(SudukoConst.SelectedBox, "").equals(position.toString() + str, ignoreCase = true)) {
                                txt.setBackgroundColor(ContextCompat.getColor(con,R.color.bolx_selected_bg))
                            } else {
                                txt.setBackgroundColor(ContextCompat.getColor(con,R.color.empty_bg))
                            }
                            txt.text = temp_sudoku[0].notes
                            recyclerView.hide()
                            txt.show()
                            txt_pos.hide()
                            txt_bg.hide()
                            txt.setTextColor(ContextCompat.getColor(con,R.color.text_color_empty))
                            txt.textSize = 10f
                            txt.setOnClickListener { view ->
                                if (temp_sudoku[0].defaultSet.equals("N",true)) {
                                    prefManager.setCustomParam(SudukoConst.SelectedBox, position.toString() + "" + view.tag)
                                    val previousSelectedPos = selectedBoxPosition
                                    selectedBoxPosition = position
                                    if (previousSelectedPos > -1){
                                        notifyItemChanged(previousSelectedPos)
                                    }
                                    notifyItemChanged(selectedBoxPosition)
                                    listener.clickOnBox()
                                }
                            }
                        } else {
                            recyclerView.hide()
                            txt.show()
                            txt_pos.hide()
                            txt_bg.hide()
//                            txt.text = ""
                            if (prefManager.getCustomParam(SudukoConst.SelectedBox, "")
                                    .equals(position.toString() + str, ignoreCase = true)
                            ) {
                                txt.setBackgroundColor(ContextCompat.getColor(con,R.color.bolx_selected_bg))
                            } else {
                                txt.setBackgroundColor(ContextCompat.getColor(con,R.color.empty_bg))
                            }
                            txt.setOnClickListener { view ->
                                if (temp_sudoku[0].defaultSet.equals("N",true)) {
                                    prefManager.setCustomParam(SudukoConst.SelectedBox,position.toString() + "" + view.tag)
                                    val previousSelectedPos = selectedBoxPosition
                                    selectedBoxPosition = position
                                    if (previousSelectedPos > -1){
                                        notifyItemChanged(previousSelectedPos)
                                    }
                                    notifyItemChanged(selectedBoxPosition)
                                    listener.clickOnBox()
                                }
                            }
                        }
                    }
                }
            }

        }

    }

    private fun SetValue(
        txt: AppCompatTextView,
        position: Int,
        txt_bg_main: AppCompatTextView,
        txt_bg_wrong: AppCompatTextView,
        recyclerView: RecyclerView
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            val str = txt.tag as String
            val temp_sudoku: List<SudukoPlay> =
                dataManager.getRoomID_CellPosition_SudokuPlay(roomId, position.toString() + str)
            CoroutineScope(Dispatchers.Main).launch {
                recyclerView.hide()
                txt.show()
                txt_bg_wrong.show()
                txt_bg_main.show()

                if (temp_sudoku.isNotEmpty()) {
                    var setvalue = ""
                    if (temp_sudoku[0].cellValue.isNotEmpty()) {
                        try {
                            setvalue = (temp_sudoku[0].cellValue.toInt() + 1).toString() + ""
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                    }
                    txt.text = setvalue
                    if (temp_sudoku[0].defaultSet.equals("Y",true)) {
                        txt.setBackgroundColor(ContextCompat.getColor(con,R.color.main_bg_not_empty))
                        txt.setTextColor(ContextCompat.getColor(con,R.color.text_color_not_empty))
                        txt.textSize = 22f
                        if (txt.text.toString().isNotEmpty()) {
                            CoroutineScope(Dispatchers.Default).launch {
                                val list_answer_temp: List<SudukoAnswerStatus> =
                                    dataManager.getRoomID_SudokuAnswer(roomId, position.toString() + str)
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (list_answer_temp.isNotEmpty()) {
                                        txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.main_bg_not_empty))
                                        txt.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                                        txt_bg_wrong.show()
                                        txt_bg_wrong.setBackgroundResource(R.drawable.round)
                                        txt.setTextColor(ContextCompat.getColor(con,R.color.text_color_wrong))
                                    } else {
                                        txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                                        txt_bg_wrong.hide()
                                    }
                                }

                            }


                        }
                    } else {

                        txt.textSize = 22f
                        txt.setTextColor(ContextCompat.getColor(con,R.color.text_color_empty))
                        if (prefManager.getCustomParam(SudukoConst.SelectedBox, "")
                                .equals(temp_sudoku[0].cellPosition, ignoreCase = true)
                        ) {
                            txt.setBackgroundColor(ContextCompat.getColor(con,R.color.bolx_selected_bg))
                            txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                            txt_bg_wrong.hide()
                        } else {
                            txt.setBackgroundColor(ContextCompat.getColor(con,R.color.empty_bg))
                        }
                        if (txt.text.toString().isNotEmpty()) {
                            CoroutineScope(Dispatchers.Default).launch {
                                val list_answer_temp: List<SudukoAnswerStatus> =
                                    dataManager.getRoomID_SudokuAnswer(roomId, position.toString() + str)
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (list_answer_temp.isNotEmpty()) {
                                        txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.empty_bg)) // new
                                        txt.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                                        txt_bg_wrong.setBackgroundResource(R.drawable.round)
                                        txt_bg_wrong.show()
                                        txt.setTextColor(ContextCompat.getColor(con,R.color.text_color_wrong))
                                        if (prefManager.getCustomParam(SudukoConst.SelectedBox, "")
                                                .equals(temp_sudoku[0].cellPosition, ignoreCase = true)
                                        ) {
                                            txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.bolx_selected_bg))
                                            txt.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                                            txt_bg_wrong.show()
                                        }
                                    } else {
                                        txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                                        txt_bg_wrong.hide()
                                    }
                                }
                            }


                        } else if (temp_sudoku[0].notes.isNotEmpty()) {
                            try {
                                txt.text = temp_sudoku[0].notes
                                txt.setTextColor(ContextCompat.getColor(con,R.color.text_color_empty))
                                //                        txt.setTypeface(txt.getTypeface(), Typeface.BOLD);
                                txt.textSize = 10f
                                txt.show()
                                txt.setBackgroundColor(ContextCompat.getColor(con,R.color.empty_bg))
                                txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                                txt_bg_wrong.hide()
                                if (prefManager.getCustomParam(SudukoConst.SelectedBox, "")
                                        .equals(temp_sudoku[0].cellPosition, ignoreCase = true)
                                ) {
                                    txt_bg_main.setBackgroundColor(ContextCompat.getColor(con,R.color.bolx_selected_bg))
                                    txt.setBackgroundColor(ContextCompat.getColor(con,R.color.transprent_bg))
                                }
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    txt.setOnClickListener {
                        if (temp_sudoku[0].defaultSet.equals("N",true)) {
                            prefManager.setCustomParam(SudukoConst.SelectedBox,temp_sudoku[0].cellPosition)
                            val previousSelectedPos = selectedBoxPosition
                            selectedBoxPosition = position
                            if (previousSelectedPos > -1){
                                notifyItemChanged(previousSelectedPos)
                            }
                            notifyItemChanged(selectedBoxPosition)
                            listener.clickOnBox()
                        }
                    }
                } else {
                }
            }

        }

    }

    init {
        this.mListData = mListData
        this.roomId = roomId
    }
}
