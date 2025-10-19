package com.example.autocallrejector.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autocallrejector.data.BlockedNumberDao

class AddNumberViewModelFactory(private val blockedNumberDao: BlockedNumberDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNumberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNumberViewModel(blockedNumberDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}