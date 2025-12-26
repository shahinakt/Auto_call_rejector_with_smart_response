package com.example.autocallrejector

import android.app.Application
import com.example.autocallrejector.data.AppDatabase

class AutoCallRejectorApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}