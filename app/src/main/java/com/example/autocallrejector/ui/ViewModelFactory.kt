package com.example.autocallrejector.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autocallrejector.data.BlockedNumberDao

class MainViewModelFactory(private val blockedNumberDao: BlockedNumberDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(blockedNumberDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}