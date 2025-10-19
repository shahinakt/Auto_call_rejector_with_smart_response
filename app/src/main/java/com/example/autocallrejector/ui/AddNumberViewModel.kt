package com.example.autocallrejector.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocallrejector.data.BlockedNumber
import com.example.autocallrejector.data.BlockedNumberDao
import kotlinx.coroutines.launch

class AddNumberViewModel(private val blockedNumberDao: BlockedNumberDao) : ViewModel() {

    fun addBlockedNumber(number: String) {
        viewModelScope.launch {
            blockedNumberDao.insert(BlockedNumber(number = number))
        }
    }
}