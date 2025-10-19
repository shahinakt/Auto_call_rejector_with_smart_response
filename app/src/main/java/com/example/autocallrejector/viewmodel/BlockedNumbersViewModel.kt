package com.example.autocallrejector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocallrejector.data.BlockedNumber
import com.example.autocallrejector.data.BlockedNumberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlockedNumbersViewModel(private val repository: BlockedNumberRepository) : ViewModel() {
    // Private mutable state for internal updates; expose read-only StateFlow for UI
    private val _blockedNumbers = MutableStateFlow<List<BlockedNumber>>(emptyList())
    val blockedNumbers: StateFlow<List<BlockedNumber>> = _blockedNumbers.asStateFlow()

    init {
        // Collect from repository's Flow (reactive: UI updates on DB changes)
        viewModelScope.launch {
            repository.allNumbers.collect { numbers ->
                _blockedNumbers.value = numbers
            }
        }
    }

    // Add number: Validate minimally, insert async on IO dispatcher (non-blocking)
    fun addNumber(number: String) {
        if (number.isBlank()) return  // Safe handling: Ignore invalid inputs
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insert(number)  // Repository handles trimming
            } catch (e: Exception) {
                // Silent fail for DB errors (extend with logging if needed)
            }
        }
    }

    // Delete number: Async on IO dispatcher
    fun deleteNumber(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.delete(id)
            } catch (e: Exception) {
                // Silent fail
            }
        }
    }
}