package com.example.autocallrejector.data

import kotlinx.coroutines.flow.Flow

class BlockedNumberRepository(private val blockedNumberDao: BlockedNumberDao) {
    val allNumbers: Flow<List<BlockedNumber>> = blockedNumberDao.getAllNumbers()

    suspend fun insert(number: String) {
        if (number.isBlank()) return
        blockedNumberDao.insert(BlockedNumber(number = number.trim()))
    }

    suspend fun delete(id: Long) {
        blockedNumberDao.deleteById(id)
    }

    suspend fun isBlocked(number: String): Boolean {
        return blockedNumberDao.isNumberBlocked(number.trim()) > 0
    }
}