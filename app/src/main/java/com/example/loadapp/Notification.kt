package com.example.loadapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object Notification{
    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //TODO: STEP[1] create a new notification channel
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            channel.enableVibration(true)


            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            //TODO: STEP[1] create a new notification channel
        }
    }

    fun createSampleDataNotification(
        context: Context,
        title: String,
        message: String,
        autoCancel: Boolean,
        status:String,
        fileName:String,
    ) {

        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {

            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(message)
            setAutoCancel(autoCancel)
            priority = NotificationCompat.PRIORITY_DEFAULT

            val pendingIntent: PendingIntent?
            val intent = Intent(context, Details::class.java)
            intent.putExtra("status", status)
            intent.putExtra("fileName", fileName)
            pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(context, 0, intent, FLAG_MUTABLE)

            } else {
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT
                )
            }

            setContentIntent(pendingIntent)
        }

        val detailsActivity = Intent(
            context,
            Details::class.java
        )

        detailsActivity.action = context.getString(R.string.action)
        detailsActivity.putExtra("status", status)
        detailsActivity.putExtra("fileName", fileName)

        val detailActivityPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, detailsActivity, FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                context,
                0,
                detailsActivity,
                PendingIntent.FLAG_ONE_SHOT
            )
        }

        notificationBuilder.addAction(
            R.drawable.ic_launcher_foreground,
            "Go to activity",
            detailActivityPendingIntent
        )


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

}
