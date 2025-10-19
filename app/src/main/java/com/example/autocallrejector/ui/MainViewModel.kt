package com.example.autocallrejector.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocallrejector.data.BlockedNumber
import com.example.autocallrejector.data.BlockedNumberDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val blockedNumberDao: BlockedNumberDao) : ViewModel() {

    val blockedNumbers: StateFlow<List<BlockedNumber>> = blockedNumberDao.getAllNumbers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addBlockedNumber(number: String) {
        viewModelScope.launch {
            blockedNumberDao.insert(BlockedNumber(number = number))
        }
    }

    fun deleteBlockedNumber(number: BlockedNumber) {
        viewModelScope.launch {
            blockedNumberDao.delete(number)
        }
    }
}