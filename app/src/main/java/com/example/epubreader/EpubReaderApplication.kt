package com.example.epubreader

import android.app.Application
import com.example.epubreader.data.local.EpubReaderDatabase

/**
 * Application class
 * 
 * Inizializza il database e le dipendenze
 */
class EpubReaderApplication : Application() {
    
    // Database instance
    val database: EpubReaderDatabase by lazy {
        EpubReaderDatabase.getInstance(this)
    }
    
    override fun onCreate() {
        super.onCreate()
    }
}
