package com.example.todoapp.ui.Service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import com.example.todoapp.ui.Activity.MainActivity

/**
Class-receiver of Intent. Creates Notification at deadline time in 00:00.
 */
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {

        // Intent with launch MainActivity from Notification
        val notifyIntent = Intent(p0!!, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Pending Intent with Intent
        val pendingIntent = PendingIntent.getActivity(
            p0, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a Notification
        var mBuilder = NotificationCompat.Builder(p0, "77")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(p1?.getStringExtra("title"))
            .setContentText(p1?.getStringExtra("text"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)    // add Intent with launch MainActivity

        val mNotificationManager =
            p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notify the created Notification
        mNotificationManager.notify(p1!!.getIntExtra("id", 0), mBuilder.build())
    }
}