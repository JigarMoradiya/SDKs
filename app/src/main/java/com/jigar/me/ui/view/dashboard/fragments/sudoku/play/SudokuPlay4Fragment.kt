package com.jigar.me.ui.view.dashboard.fragments.sudoku.play

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jigar.me.MyApplication
import com.jigar.me.R
import com.jigar.me.data.local.data.DataProvider.getColumnList_ForSudoku4
import com.jigar.me.data.local.data.DataProvider.getGridList_ForSudoku4
import com.jigar.me.data.local.data.DataProvider.getRowList_ForSudoku4
import com.jigar.me.data.model.dbtable.suduko.SudukoAnswerStatus
import com.jigar.me.data.model.dbtable.suduko.SudukoLevel
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.databinding.FragmentSudokuPlay4by4Binding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.dialogs.sudoku.SudokuCompleteDialog
import com.jigar.me.ui.view.dashboard.fragments.sudoku.play.adapter.Play4By4Adapter
import com.jigar.me.ui.viewmodel.SudokuViewModel
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.PlaySound
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.sudoku.SudukoConst
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SudokuPlay4Fragment : BaseFragment(), SudokuCompleteDialog.SudokuCompleteDialogInterface,
    Play4By4Adapter.ListClicks {
    private val sudokuViewModel by viewModels<SudokuViewModel>()
    private lateinit var binding: FragmentSudokuPlay4by4Binding
    private lateinit var adapter: Play4By4Adapter
    private var roomId = ""
    private var level = ""
    private var currentDate: String = ""

    private val list_suduko = ArrayList<String>()
    private val list_grid = ArrayList<String>()
    private val list_column = ArrayList<String>()
    private val list_row = ArrayList<String>()
    private var list_notes = ArrayList<String>()

    private var mp_select: MediaPlayer? = null
    private  var mp_delete: MediaPlayer? = null
    private  var mp_set: MediaPlayer? = null

    private val h: Handler = Handler(Looper.getMainLooper())
    private val delay = 1000
    private var runnable: Runnable? = null
    private var timer = 0
    private lateinit var mNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = SudokuPlay4FragmentArgs.fromBundle(requireArguments()).roomId
        level = SudokuPlay4FragmentArgs.fromBundle(requireArguments()).level
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSudokuPlay4by4Binding.inflate(inflater, container, false)
        setNavigationGraph()
        initView()
        initListener()
        adsBanner()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun initView() {
        mp_select = MediaPlayer.create(requireContext(), R.raw.sudoku_select)
        mp_delete = MediaPlayer.create(requireContext(), R.raw.sudoku_number_delete)
        mp_set = MediaPlayer.create(requireContext(), R.raw.sudoku_number_click)

        list_grid.addAll(getGridList_ForSudoku4())
        list_column.addAll(getColumnList_ForSudoku4())
        list_row.addAll(getRowList_ForSudoku4())
        checkData()
        setData()
    }
    private fun initListener() {
        setSoundIcon()
        binding.cardVolume.onClick {
            if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y") == "y"){
                prefManager.setCustomParam(AppConstants.Settings.Setting_SudokuVolume,"")
            }else{
                prefManager.setCustomParam(AppConstants.Settings.Setting_SudokuVolume,"y")
            }
            setSoundIcon()
        }
        binding.cardBack.onClick {
            onBack()
        }
        binding.txtNotes.onClick {
            if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y").equals("y")) {
                mp_set?.start()
            }
            if (prefManager.getCustomParam(SudukoConst.Notes, "0") == "0") {
                prefManager.setCustomParam(SudukoConst.Notes, "1")
                notesSet()
            } else {
                notesRemove()
            }
        }
        binding.txtHint.onClick {
            if (!requireContext().isNetworkAvailable) {
                showToast(R.string.no_internet)
                return@onClick
            }
            if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y") == "y") {
                mp_set?.start()
            }
            if (prefManager.getCustomParam(SudukoConst.SelectedBox, "").isNotEmpty()) {
                hintSet()
                lifecycleScope.launch {
                    delay(300)
                    ads()
                }
            } else {
                showToast(R.string.txt_select_box)
            }
        }
        binding.txt0.onClick { setValueNumberClick("0") }
        binding.txt1.onClick { setValueNumberClick("1") }
        binding.txt2.onClick { setValueNumberClick("2") }
        binding.txt3.onClick { setValueNumberClick("3") }
        binding.imgEarse.onClick { setValueNumberClick("") }
    }
    private fun checkData() {
        //milliseconds
        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MMM-yyyy")
        currentDate = df.format(c.time)
        var check_status = ""
        lifecycleScope.launch {
            val lsit_temp: List<SudukoLevel> = sudokuViewModel.getRoomID_SudokuLevel(roomId)
            if (lsit_temp.isEmpty()) {
                sudokuViewModel.insert_SudokuLevel(SudukoLevel(0, "", "0", roomId, timer.toString() + ""))
            } else {
                val list = sudokuViewModel.getRoomID_SudokuAnswer(roomId)
                // means not completed, double check
                if (list.isEmpty()){
                    check_status = lsit_temp[0].status
                }
                try {
                    timer = lsit_temp[0].playTime.toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }


            }

            if (!check_status.equals("1", ignoreCase = true)) {
                h.postDelayed(object : Runnable {
                    override fun run() {
                        //do something
                        runnable = this
                        timer++
                        val hours = timer / 3600
                        val minutes = timer % 3600 / 60
                        val seconds = timer % 60
                        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        binding.txtTimer.text = timeString
                        lifecycleScope.launch {
                            sudokuViewModel.updatePlayTime_SudokuLevel(timer.toString() + "", roomId)
                        }
                        h.postDelayed(this, delay.toLong())
                    }
                }, delay.toLong())
            } else {
                completeSudokuDailog()
                binding.linearRight.hide()
                val hours = timer / 3600
                val minutes = timer % 3600 / 60
                val seconds = timer % 60
                val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.txtTimer.text = timeString
            }

        }

    }

    private fun setData() {
        list_suduko.clear()
        lifecycleScope.launch {
            for (i in 0..3) {
                list_suduko.add(i.toString() + "")
                val isWrongAnswerNotThere = sudokuViewModel.getRoomID_SudokuAnswer_CheckCellValue(roomId, i.toString() + "").isEmpty()
                val isTrue = sudokuViewModel.getRoomID_CellValue(roomId, i.toString() + "").size == 4
                if (isTrue && isWrongAnswerNotThere) {
                    buttonDisable(i.toString() + "")
                } else {
                    buttonEnable(i.toString() + "")
                }
            }

        }

        adapter = Play4By4Adapter(requireContext(), list_suduko, roomId,sudokuViewModel,prefManager,this)
        binding.gridView.adapter = adapter
    }

    private fun buttonDisable(pre_value: String) {
        if (pre_value.equals("0", ignoreCase = true)) {
            binding.txt0.setBackgroundResource(R.drawable.ractagle_gray500_border)
            binding.txt0.isEnabled = false
        } else if (pre_value.equals("1", ignoreCase = true)) {
            binding.txt1.setBackgroundResource(R.drawable.ractagle_gray500_border)
            binding.txt1.isEnabled = false
        } else if (pre_value.equals("2", ignoreCase = true)) {
            binding.txt2.setBackgroundResource(R.drawable.ractagle_gray500_border)
            binding.txt2.isEnabled = false
        } else if (pre_value.equals("3", ignoreCase = true)) {
            binding.txt3.setBackgroundResource(R.drawable.ractagle_gray500_border)
            binding.txt3.isEnabled = false
        }
    }

    private fun buttonEnable(pre_value: String) {
        if (pre_value.equals("0", ignoreCase = true)) {
            binding.txt0.setBackgroundResource(R.drawable.ractagle_white_border)
            binding.txt0.isEnabled = true
        } else if (pre_value.equals("1", ignoreCase = true)) {
            binding.txt1.setBackgroundResource(R.drawable.ractagle_white_border)
            binding.txt1.isEnabled = true
        } else if (pre_value.equals("2", ignoreCase = true)) {
            binding.txt2.setBackgroundResource(R.drawable.ractagle_white_border)
            binding.txt2.isEnabled = true
        } else if (pre_value.equals("3", ignoreCase = true)) {
            binding.txt3.setBackgroundResource(R.drawable.ractagle_white_border)
            binding.txt3.isEnabled = true
        }
    }
    override fun clickOnBox() {
        hintRemove()
        if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y").equals("y",true)) {
            mp_select?.start()
        }
        if (prefManager.getCustomParam(SudukoConst.Notes, "0") == "1") {
            setNote()
        }
    }

    private fun setNote() {
        binding.txt0.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        binding.txt1.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        binding.txt2.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        binding.txt3.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        lifecycleScope.launch {
            val notes: String =
                sudokuViewModel.getCellNotes_SudokuPlay(roomId, prefManager.getCustomParam(SudukoConst.SelectedBox, ""))
            list_notes.clear()
            if (notes.isNotEmpty()) {
                list_notes = ArrayList(Arrays.asList(*notes.split(",".toRegex()).toTypedArray()))
                for (i in 1..4) {
                    if (notes.contains(i.toString() + "")) {
                        buttonNotesSet(i.toString() + "")
                    } else {
                        buttonNotesRemain(i.toString() + "")
                    }
                }

            }
        }

    }
    private fun notesSet() {
        binding.txtNotes.setBackgroundResource(R.drawable.ractagle_notes)
        binding.txtHint.isEnabled = false
//        if (prefManager.getCustomParam(SudukoConst.Hintdate, "") == currentDate) {
//            binding.txtHint.setBackgroundResource(R.drawable.ractagle_hint)
//        } else {
//            binding.txtHint.setBackgroundResource(R.drawable.ractagle_gray)
//        }
        binding.txtHint.setBackgroundResource(R.drawable.ractagle_gray)
        binding.imgEarse.setBackgroundResource(R.drawable.ractagle_gray)
        binding.imgEarse.isEnabled = false
        binding.txt0.isEnabled = true
        binding.txt1.isEnabled = true
        binding.txt2.isEnabled = true
        binding.txt3.isEnabled = true
        lifecycleScope.launch {
            val notes: String = sudokuViewModel.getCellNotes_SudokuPlay(roomId, prefManager.getCustomParam(SudukoConst.SelectedBox, ""))
            list_notes.clear()
            if (notes.isNotEmpty()) {
                list_notes = ArrayList(listOf(*notes.split(",".toRegex()).toTypedArray()))
                for (i in 1..4) {
                    if (notes.contains(i.toString() + "")) {
                        buttonNotesSet(i.toString() + "")
                    } else {
                        buttonNotesRemain(i.toString() + "")
                    }
                }
            } else {
                binding.txt0.setBackgroundResource(R.drawable.ractagle_notes_set)
                binding.txt1.setBackgroundResource(R.drawable.ractagle_notes_set)
                binding.txt2.setBackgroundResource(R.drawable.ractagle_notes_set)
                binding.txt3.setBackgroundResource(R.drawable.ractagle_notes_set)
            }
        }
        adapter.notifyItemChanged(adapter.selectedBoxPosition)

    }

    private fun notesRemove() {
        binding.txtNotes.setBackgroundResource(R.drawable.ractagle_white_border)
        binding.imgEarse.setBackgroundResource(R.drawable.ractagle_white_border)
        binding.imgEarse.isEnabled = true
        binding.txtHint.isEnabled = true
//        if (prefManager.getCustomParam(SudukoConst.Hintdate, "") == currentDate) {
//            binding.txtHint.setBackgroundResource(R.drawable.ractagle_hint)
//        } else {
//            binding.txtHint.setBackgroundResource(R.drawable.ractagle_white_border)
//        }
        binding.txtHint.setBackgroundResource(R.drawable.ractagle_white_border)
        prefManager.setCustomParam(SudukoConst.Notes, "0")
        hintRemove()
        adapter.notifyItemChanged(adapter.selectedBoxPosition)
    }

    private fun hintRemove() {
        lifecycleScope.launch {
            if (prefManager.getCustomParam(SudukoConst.Notes, "0") == "0") {
                binding.txtNotes.setBackgroundResource(R.drawable.ractagle_white_border)
                for (i in 0..3) {
                    val isWrongAnswerNotThere = sudokuViewModel.getRoomID_SudokuAnswer_CheckCellValue(roomId, i.toString() + "").isEmpty()
                    val isTrue = sudokuViewModel.getRoomID_CellValue_SudokuPlay(roomId, i.toString() + "").size == 4
                    if (isTrue && isWrongAnswerNotThere) {
                        buttonDisable(i.toString() + "")
                    } else {
                        buttonEnable(i.toString() + "")
                    }
                }
            } else {
            }
        }

    }

    private fun hintSet() {
        val str_position: String = prefManager.getCustomParam(SudukoConst.SelectedBox, "")
        lifecycleScope.launch {
            val pre_value: String = sudokuViewModel.getCellValue_SudokuPlay(roomId, str_position)
            if (pre_value.isEmpty()) {
                binding.txtHint.setBackgroundResource(R.drawable.ractagle_white_border)
                var str_row = ""
                var str_column = ""
                var str_grid = ""
                for (j in 0..3) {
                    if (list_column[j].contains(str_position)) {
                        str_column = list_column[j]
                    }
                    if (list_row[j].contains(str_position)) {
                        str_row = list_row[j]
                    }
                    if (list_grid[j].contains(str_position)) {
                        str_grid = list_grid[j]
                    }
                }
                val list_suduko1 = ArrayList<String>()
                list_suduko1.addAll(list_suduko)
                    val temp_list_number: ArrayList<String> = sudokuViewModel.getRoomID_CellPosition_IN_List_For_Remove_SudokuPlay(roomId, str_column+ "," + str_grid + "," + str_row, list_suduko1)
                    for (i in temp_list_number.indices) {
                        buttonHintShow(temp_list_number[i])
                    }
                prefManager.setCustomParam(SudukoConst.Hintdate, currentDate)
            } else {
                showToast(R.string.txt_select_box_empty)
            }
        }



    }

    private fun buttonHintShow(pre_value: String) {
        if (pre_value.equals("0", ignoreCase = true)) {
            binding.txt0.setBackgroundResource(R.drawable.ractagle_hint_set)
        } else if (pre_value.equals("1", ignoreCase = true)) {
            binding.txt1.setBackgroundResource(R.drawable.ractagle_hint_set)
        } else if (pre_value.equals("2", ignoreCase = true)) {
            binding.txt2.setBackgroundResource(R.drawable.ractagle_hint_set)
        } else if (pre_value.equals("3", ignoreCase = true)) {
            binding.txt3.setBackgroundResource(R.drawable.ractagle_hint_set)
        }
    }


    // TODO Notes
    private fun setValueNumberClick(iii: String) {
        if (prefManager.getCustomParam(SudukoConst.Notes, "0") == "0") {
            setvalue(iii)
        } else {

            if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y") == "y") {
                mp_set?.start()
            }
            if (prefManager.getCustomParam(SudukoConst.SelectedBox, "").isNotEmpty()) {
                val iii1 = (iii.toInt() + 1).toString() + ""
                if (list_notes.contains(iii1)) {
                    list_notes.remove(iii1)
                } else {
                    list_notes.add(iii1)
                }
                list_notes.sortWith { fruit2, fruit1 -> fruit2.compareTo(fruit1) }
                val csv = TextUtils.join(",", list_notes)
                lifecycleScope.launch {
                    sudokuViewModel.updateCellNotesPlay(csv,prefManager.getCustomParam(SudukoConst.SelectedBox, ""),roomId)
                }
                setNote()
                adapter.notifyItemRangeChanged(0,list_suduko.size)
            } else {
                showToast(R.string.txt_select_box)
            }
        }
    }


    private fun setvalue(iii: String) {
        lifecycleScope.launch {
            var pre_value = ""
            if (prefManager.getCustomParam(SudukoConst.SelectedBox, "").isNotEmpty()) {
                pre_value = sudokuViewModel.getCellValue_SudokuPlay(roomId,prefManager.getCustomParam(SudukoConst.SelectedBox, ""))
                sudokuViewModel.deleteAllAnswer(roomId)
                sudokuViewModel.updateCellValuePlay(iii,prefManager.getCustomParam(SudukoConst.SelectedBox, ""), roomId)
                if (iii.isEmpty()) {
                    if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y").equals("y") && pre_value.isNotEmpty()) {
                        mp_delete?.start()
                    }
                } else {
                    if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y").equals("y")) {
                        mp_set?.start()
                    }
                }

                val temp_sudoku: List<SudukoPlay> =
                    sudokuViewModel.getRoomID_DefaultSet_SudokuPlay(roomId, "N")
                for (k in temp_sudoku.indices) {
                    val str_value: String = temp_sudoku[k].cellValue
                    val str_position: String = temp_sudoku[k].cellPosition

                    var str_row = ""
                    var str_column = ""
                    var str_grid = ""
                    for (j in 0..3) {
                        if (list_column[j].contains(str_position)) {
                            str_column = list_column[j]
                        }
                        if (list_row[j].contains(str_position)) {
                            str_row = list_row[j]
                        }
                        if (list_grid[j].contains(str_position)) {
                            str_grid = list_grid[j]
                        }
                    }
                    val lst_temp: List<String> = sudokuViewModel.getRoomID_CellPosition_IN_Quate_SudokuPlay(str_value + "", roomId,"$str_column,$str_row,$str_grid")
                    val str_temp: String = lst_temp.joinToString(",")
                    if (str_temp.contains(",")) {
                        val lst_temp1: List<String> = sudokuViewModel.getRoomID_CellPosition_IN_SudokuPlay(
                            str_value + "", roomId,
                            "$str_column,$str_row,$str_grid"
                        )
                        if (lst_temp1.isNotEmpty()) {
                            for (ii in lst_temp1.indices) {
                                val str = lst_temp1[ii]
                                sudokuViewModel.insertSudukoAnswer(SudukoAnswerStatus(0,temp_sudoku[k].cellPosition,str_value + "",roomId,str + ""))
                            }
                        }
                    }
                }


                adapter.notifyItemRangeChanged(0,list_suduko.size)
                hintRemove()

                if (iii.isNotEmpty()) {
                    val isEmpty = sudokuViewModel.getRoomID_SudokuAnswer(roomId).isEmpty()

                    if ((sudokuViewModel.getRoomID_CellValue_SudokuPlay(roomId, "0").size == 4) &&
                        (sudokuViewModel.getRoomID_CellValue_SudokuPlay(roomId, "1").size == 4) &&
                        (sudokuViewModel.getRoomID_CellValue_SudokuPlay(roomId, "2").size == 4) &&
                        (sudokuViewModel.getRoomID_CellValue_SudokuPlay(roomId, "3").size == 4) && isEmpty){
                        sudokuViewModel.updateStatus_SudokuLevel("1", roomId)
                        h.removeCallbacks((runnable)!!)
                        //                    Utils.showToast(PlayActivity.this, getResources().getString(R.string.txt_completed_level));
                        binding.linearRight.hide()
                        if (prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y") == "y") {
                            PlaySound.play(requireContext(), PlaySound.clap_click)
                        }
                        // event log for sudoku
                        MyApplication.logEvent(AppConstants.FirebaseEvents.Sudoku, Bundle().apply {
                            putString(AppConstants.FirebaseEvents.deviceId, prefManager.getDeviceId())
                            putString(AppConstants.FirebaseEvents.SudokuLevel, level)
                            putBoolean(AppConstants.FirebaseEvents.SudokuLevelComplete, true)
                        })
                        ads()
                        completeSudokuDailog()
                    }
                }
            } else {
                showToast(resources.getString(R.string.txt_select_box))
            }
        }
    }

    private fun buttonNotesSet(pre_value: String) {
        if (pre_value.equals("1", ignoreCase = true)) {
            binding.txt0.setBackgroundResource(R.drawable.ractagle_notes_set)
        } else if (pre_value.equals("2", ignoreCase = true)) {
            binding.txt1.setBackgroundResource(R.drawable.ractagle_notes_set)
        } else if (pre_value.equals("3", ignoreCase = true)) {
            binding.txt2.setBackgroundResource(R.drawable.ractagle_notes_set)
        } else if (pre_value.equals("4", ignoreCase = true)) {
            binding.txt3.setBackgroundResource(R.drawable.ractagle_notes_set)
        }
    }

    private fun buttonNotesRemain(pre_value: String) {
        if (pre_value.equals("1", ignoreCase = true)) {
            binding.txt0.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        } else if (pre_value.equals("2", ignoreCase = true)) {
            binding.txt1.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        } else if (pre_value.equals("3", ignoreCase = true)) {
            binding.txt2.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        } else if (pre_value.equals("4", ignoreCase = true)) {
            binding.txt3.setBackgroundResource(R.drawable.ractagle_notes_set_remain)
        }
    }
    private fun setSoundIcon() {
        val isSoundOn = prefManager.getCustomParam(AppConstants.Settings.Setting_SudokuVolume, "y") == "y"
        if (isSoundOn){
            binding.imgVolume.setImageResource(R.drawable.ic_volume_on)
            binding.txtVolume.text = getString(R.string.volume_on)
        }else {
            binding.imgVolume.setImageResource(R.drawable.ic_volume_off)
            binding.txtVolume.text = getString(R.string.volume_off)
        }
    }

    override fun onPause() {
        super.onPause()
        runnable?.let { h.removeCallbacks(it) }//stop handler when activity not visible
    }

    private fun onBack() {
        mNavController.navigateUp()
    }

    private fun completeSudokuDailog() {
        val hours = timer / 3600
        val minutes = timer % 3600 / 60
        val seconds = timer % 60
        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        SudokuCompleteDialog.showPopup(requireContext(),timeString,this)
    }

    override fun sudokuCompleteDialogCloseClick() {
        onBack()
    }
    private fun adsBanner() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" &&
            prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" &&
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")
        ) {
            showAMBannerAds(binding.adView,getString(R.string.banner_ad_unit_id_sudoku))
        }
    }
    private fun ads() {
        if (requireContext().isNetworkAvailable && AppConstants.Purchase.AdsShow == "Y" &&
            prefManager.getCustomParam(AppConstants.AbacusProgress.Ads,"") == "Y" &&
            (prefManager.getCustomParam(AppConstants.Purchase.Purchase_All,"") != "Y" // if not purchased
                    && prefManager.getCustomParam(AppConstants.Purchase.Purchase_Ads,"") != "Y")
        ) {
            newInterstitialAd(getString(R.string.interstitial_ad_unit_id_sudoku))
        }

    }

}