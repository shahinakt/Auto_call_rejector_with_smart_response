package com.example.autocallrejector

import android.app.Application
import com.example.autocallrejector.data.AppDatabase

class AutoCallRejectorApplication : Application() {
    // Singleton database instance for app-wide access (thread-safe via Room)
    val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        // Initialize database on app start (lazy-loaded)
    }
}