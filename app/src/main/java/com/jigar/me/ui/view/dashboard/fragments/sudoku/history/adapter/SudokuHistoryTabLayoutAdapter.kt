package com.jigar.me.ui.view.dashboard.fragments.sudoku.history.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jigar.me.ui.view.dashboard.fragments.sudoku.history.SudokuHistoryListFragment

class SudokuHistoryTabLayoutAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return SudokuHistoryListFragment.newInstance(position)
    }

    override fun getItemCount(): Int {
        return 3
    }

}