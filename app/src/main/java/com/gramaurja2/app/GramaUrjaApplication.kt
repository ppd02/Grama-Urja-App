package com.gramaurja2.app

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GramaUrjaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        runCatching { FirebaseDatabase.getInstance().setPersistenceEnabled(true) }
    }
}
