package com.example.autocallrejector.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedNumberDao {
    // Get all blocked numbers as a simple list (for one-off checks)
    @Query("SELECT * FROM blocked_numbers")
    suspend fun getAll(): List<BlockedNumber>

    // Get all blocked numbers as reactive Flow (observes changes for UI updates)
    @Query("SELECT * FROM blocked_numbers ORDER BY id ASC")
    fun getAllNumbers(): Flow<List<BlockedNumber>>

    // Insert a new number (returns the inserted ID; ignores conflicts like duplicates)
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // Ignore if number already exists (simple dedup)
    suspend fun insert(number: BlockedNumber): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNumber(blockedNumber: BlockedNumber)

    // Delete a number by object
    @Delete
    suspend fun delete(number: BlockedNumber)

    // Delete a number by ID
    @Query("DELETE FROM blocked_numbers WHERE id = :id")
    suspend fun deleteNumber(id: Long)

    // Check if a number exists (for call screening; returns count > 0)
    @Query("SELECT COUNT(*) FROM blocked_numbers WHERE number = :number")
    suspend fun isNumberBlocked(number: String): Int
}