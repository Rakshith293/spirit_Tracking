package com.prathik.smartworkforce

import android.app.Application
import com.prathik.smartworkforce.data.AppDatabase

class SmartWorkforceApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        DataRepository.initialize(database)
    }
}
