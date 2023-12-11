package com.sdk.ui.interfaces

import com.sdk.data.Chip

interface RemoveTagClickListner {
    fun onRemoveClicked(chip: Chip)
    fun onItemClicked(chip: Chip)
}