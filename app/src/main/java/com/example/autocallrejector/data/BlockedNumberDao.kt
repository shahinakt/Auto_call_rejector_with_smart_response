package com.example.autocallrejector.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedNumberDao {
    @Query("SELECT * FROM blocked_numbers ORDER BY id ASC")
    fun getAllNumbers(): Flow<List<BlockedNumber>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(number: BlockedNumber): Long

    @Delete
    suspend fun delete(number: BlockedNumber)

    @Query("DELETE FROM blocked_numbers WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM blocked_numbers WHERE number = :number")
    suspend fun isNumberBlocked(number: String): Int
}