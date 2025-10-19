package com.example.autocallrejector.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_numbers")
data class BlockedNumber(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,  // Auto-generated unique ID
    val number: String  // Phone number (stored as string for flexibility, e.g., +1-123)
)