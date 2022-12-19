package com.example.loadapp

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import com.example.loadapp.Notification.createNotificationChannel

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(
            this,
            NotificationManager.IMPORTANCE_DEFAULT,
            showBadge = true,
            name = getString(R.string.app_name),
            description ="Loading App Notification" ,
        )
    }
}