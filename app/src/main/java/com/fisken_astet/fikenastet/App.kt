package com.fisken_astet.fikenastet

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ProcessLifecycleOwner
import com.fisken_astet.fikenastet.base.AppLifecycleListener
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    private var isRestarting: Boolean = false

    fun onLogout() {
        restartApp()
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleListener(this@App))
    }

    private fun restartApp() {
        if (!isRestarting) {
            isRestarting = true
            val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
