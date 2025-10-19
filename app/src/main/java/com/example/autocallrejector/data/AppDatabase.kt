package com.example.autocallrejector.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [BlockedNumber::class],
    version = 1,
    exportSchema = false  // Minimal; no schema export for simplicity
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blockedNumberDao(): BlockedNumberDao

    // Singleton instance (thread-safe; created lazily)
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "blocked_numbers_db"  // Database file name
                )
                .addCallback(object : RoomDatabase.Callback() {
                    // Optional: Populate with sample data on create (remove for production)
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Example: Pre-populate if needed (not used here)
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}