package com.jigar.me.ui.view.dashboard.fragments.sudoku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jigar.me.R
import com.jigar.me.databinding.FragmentSudokuHomeBinding
import com.jigar.me.internal.workmanager.Sudoku4WorkManager
import com.jigar.me.internal.workmanager.Sudoku6WorkManager
import com.jigar.me.internal.workmanager.Sudoku9WorkManager
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.dialogs.sudoku.AboutSudokuDialog
import com.jigar.me.ui.view.confirm_alerts.dialogs.sudoku.SudokuSelectLevelDialog
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.sudoku.SudukoConst
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SudokuHomeFragment : BaseFragment(), SudokuSelectLevelDialog.SudokuSelectLevelDialogInterface {

    private lateinit var binding: FragmentSudokuHomeBinding
    private lateinit var mNavController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSudokuHomeBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initListener()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(workRestoreBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(workRestoreBroadcastReceiver, IntentFilter(
            AppConstants.WORK_MANAGER_SUDUKO_CREATE_STATUS) )
    }

    fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.cardHistory.onClick { mNavController.navigate(R.id.action_sudokuHomeFragment_to_sudokuHistoryFragment) }
        binding.txtStartSudoku.onClick { onSudokuStartClick() }
        binding.cardAboutSudoku.onClick {
            AboutSudokuDialog.showPopup(requireContext())
        }
    }

    private fun onSudokuStartClick() {
        var level = ""
        when {
            binding.rdSudoku4.isChecked -> {
                level = SudukoConst.Level_4By4
            }
            binding.rdSudoku6.isChecked -> {
                level = SudukoConst.Level_6By6
            }
            binding.rdSudoku9.isChecked -> {
                level = SudukoConst.Level_9By9
            }
        }
        if (level.isEmpty()){
            showToast(R.string.select_sudoku_type)
        }else{
            if (level == SudukoConst.Level_9By9){
                SudokuSelectLevelDialog.showPopup(requireContext(),this)
            }else{
                sudokuSelectLevelClick(level)
            }
        }
    }

    override fun sudokuSelectLevelClick(level: String) {
        if (requireContext().isNetworkAvailable){
            showLoading()
            startWorkManager(level)
        }else{
            showToast(R.string.no_internet)
        }
    }

    private fun startWorkManager(level: String) {
        val roomId = prefManager.getCustomParamInt(SudukoConst.totalSudoku,0) + 1
        when (level) {
            SudukoConst.Level_4By4 -> {
                startCreatingSudokoWorkmanager4(roomId, level)
            }
            SudukoConst.Level_6By6 -> {
                startCreatingSudokoWorkmanager6(roomId, level)
            }
            else -> {
                startCreatingSudokoWorkmanager(roomId, level)
            }
        }
    }


    private fun startCreatingSudokoWorkmanager(roomId : Int,level : String) {
        val data = Data.Builder()
            .putString(SudukoConst.Level, level)
            .putInt(SudukoConst.RoomId, roomId)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(
            OneTimeWorkRequestBuilder<Sudoku9WorkManager>()
                .setInputData(data)
                .addTag("Sudoku9WorkManager")
                .build()
        )

    }

    private fun startCreatingSudokoWorkmanager4(roomId : Int,level : String) {
        val data = Data.Builder()
            .putString(SudukoConst.Level, level)
            .putInt(SudukoConst.RoomId, roomId)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(
            OneTimeWorkRequestBuilder<Sudoku4WorkManager>()
                .setInputData(data)
                .addTag("sudoku4WorkManager")
                .build()
        )

    }

    private fun startCreatingSudokoWorkmanager6(roomId : Int,level : String) {
        val data = Data.Builder()
            .putString(SudukoConst.Level, level)
            .putInt(SudukoConst.RoomId, roomId)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(
            OneTimeWorkRequestBuilder<Sudoku6WorkManager>()
                .setInputData(data)
                .addTag("Sudoku6WorkManager")
                .build()
        )
    }

    private val workRestoreBroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e("BroadcastReceiver", "Work Complete."+intent?.getBooleanExtra("startAgain",false))
            if (intent?.getBooleanExtra("startAgain",false) == true){
                startWorkManager(intent.getStringExtra(SudukoConst.Level)?:SudukoConst.Level_Easy)
            }else{
                hideLoading()
                prefManager.setCustomParam(SudukoConst.Notes, "0")
                prefManager.setCustomParam(SudukoConst.SelectedBox, "")

                val roomId = intent?.getStringExtra(SudukoConst.RoomId)?:""
                val level = intent?.getStringExtra(SudukoConst.Level)?:""
                when (level) {
                    SudukoConst.Level_4By4 -> {
                        val action = SudokuHomeFragmentDirections.actionSudokuHomeFragmentToSudokuPlay4Fragment(roomId,level)
                        mNavController.navigate(action)
                    }
                    SudukoConst.Level_6By6 -> {
                        val action = SudokuHomeFragmentDirections.actionSudokuHomeFragmentToSudokuPlay6Fragment(roomId,level)
                        mNavController.navigate(action)
                    }
                    else -> {
                        val action = SudokuHomeFragmentDirections.actionSudokuHomeFragmentToSudokuPlay9Fragment(roomId,level)
                        mNavController.navigate(action)
                    }
                }
            }

        }
    }

}