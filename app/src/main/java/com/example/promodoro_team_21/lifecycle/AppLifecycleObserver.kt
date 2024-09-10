package com.example.promodoro_team_21.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(private val onAppInBackground: () -> Unit) : DefaultLifecycleObserver {

    private var isInBackground = false

    override fun onStop(owner: LifecycleOwner) {
        isInBackground = true
        Log.d("AppLifecycleObserver", "App is in background")
    }

    override fun onStart(owner: LifecycleOwner) {
        isInBackground = false
        Log.d("AppLifecycleObserver", "App is in foreground")
    }

    fun isAppInBackground(): Boolean {
        return isInBackground
    }
}