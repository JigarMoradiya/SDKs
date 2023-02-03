package com.jigar.me.ui.view.dashboard.fragments.sudoku.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jigar.me.data.model.dbtable.suduko.SudukoPlay
import com.jigar.me.databinding.FragmentSudokuHistoryListBinding

import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.dashboard.fragments.exam.history.ExamHistoryHomeFragment
import com.jigar.me.ui.view.dashboard.fragments.exam.history.ExamHistoryTabFragment
import com.jigar.me.ui.view.dashboard.fragments.sudoku.SudokuHomeFragmentDirections
import com.jigar.me.ui.view.dashboard.fragments.sudoku.history.adapter.SudokuHistoryListAdapter
import com.jigar.me.ui.viewmodel.SudokuViewModel
import com.jigar.me.utils.extensions.hide
import com.jigar.me.utils.extensions.show
import com.jigar.me.utils.sudoku.SudukoConst
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SudokuHistoryListFragment :
    BaseFragment(), SudokuHistoryListAdapter.ListClicks{
    private val sudokuViewModel by viewModels<SudokuViewModel>()
    private lateinit var binding: FragmentSudokuHistoryListBinding
    private lateinit var listAdapter: SudokuHistoryListAdapter
    companion object {
        const val ARG_POSITION = "arg_position"

        @JvmStatic
        fun newInstance(position: Int) = SudokuHistoryListFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSudokuHistoryListBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        listAdapter = SudokuHistoryListAdapter(this, arrayListOf(),sudokuViewModel)
        binding.recyclerviewPage.adapter = listAdapter
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        val sudokuType = when (arguments?.getInt(ExamHistoryTabFragment.ARG_POSITION)) {
            0 -> { SudukoConst.Level_4By4 }
            1 -> { SudukoConst.Level_6By6 }
            else -> { SudukoConst.Level_9By9 }
        }
        lifecycleScope.launch {
            val list = sudokuViewModel.getSudokuList(sudokuType)
            fetchList(list)
        }
    }

    private fun fetchList(data: List<SudukoPlay>) {
        if (data.isNullOrEmpty()){
            binding.noDataView.show()
            binding.recyclerviewPage.hide()
        }else{
            binding.noDataView.hide()
            binding.recyclerviewPage.show()
            listAdapter.setList(data)
        }
    }

    override fun itemClick(position: Int, type: String) {
        val roomId = listAdapter.mListData[position].roomID
        val level = listAdapter.mListData[position].level

        prefManager.setCustomParam(SudukoConst.Notes, "0")
        prefManager.setCustomParam(SudukoConst.SelectedBox, "")
        val mNavController = ((parentFragment as SudokuHistoryFragment)).mNavController
        when (listAdapter.mListData[position].level) {
            SudukoConst.Level_4By4 -> {
                val action = SudokuHistoryFragmentDirections.actionSudokuHistoryFragmentToSudokuPlay4Fragment(roomId,level)
                mNavController.navigate(action)
            }
            SudukoConst.Level_6By6 -> {
                val action = SudokuHistoryFragmentDirections.actionSudokuHistoryFragmentToSudokuPlay6Fragment(roomId,level)
                mNavController.navigate(action)
            }
            else -> {
                val action = SudokuHistoryFragmentDirections.actionSudokuHistoryFragmentToSudokuPlay9Fragment(roomId,level)
                mNavController.navigate(action)
            }
        }
    }




}