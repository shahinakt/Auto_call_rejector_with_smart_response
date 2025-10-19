package com.example.autocallrejector.data

import kotlinx.coroutines.flow.Flow

class BlockedNumberRepository(private val blockedNumberDao: BlockedNumberDao) {
    // Expose reactive list of all numbers
    val allNumbers: Flow<List<BlockedNumber>> = blockedNumberDao.getAllNumbers()

    // Async insert: Create entity and insert via DAO (suspend for coroutine use)
    suspend fun insert(number: String) {
        if (number.isNotBlank()) {  // Basic validation (trimmed non-empty)
            val entity = BlockedNumber(number = number.trim())  // Trim input for cleanliness
            blockedNumberDao.insertNumber(entity)
        }
    }

    // Async delete by ID
    suspend fun delete(id: Long) {
        blockedNumberDao.deleteNumber(id)
    }

    // Async check if blocked (for service; returns true if count > 0)
    suspend fun isBlocked(number: String): Boolean {
        return blockedNumberDao.isNumberBlocked(number.trim()) > 0
    }
}