package com.example.todoapp.ui.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Service.MyReceiver

/**
 * Static class for create and cancel Notification Alarms.
 */
object NotificAlarmManager {

    // Create Notification alarm at specific time.
    // Params:
    // ~ context: Context of application;
    // ~ todoItem: Information for Notification;
    // ~ triggerAtMillis: Alarm trigger in milliseconds.
    fun setNotificationAlarm(context: Context, todoItem: TodoItem, triggerAtMillis: Long) {
        // Intent for creating Notification
        val intent = Intent(context, MyReceiver::class.java)
        intent.action = todoItem.id.toString()
        intent.putExtra("id", todoItem.id)
        intent.putExtra("title", todoItem.textCase)
        intent.putExtra("text", "Дата дедлайна: " + todoItem.deadlineData)

        // Alarm Manager
        val am = ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager

        // Pending Intent
        val pendingIntent = PendingIntent.getBroadcast(
            context, todoItem.id, intent, 0
        )

        // Set alarm for specific time
        am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    // Cancel Notification alarm
    fun cancelNotificationAlarm(context: Context, todoItem_id: Int) {
        // Intent
        val intent = Intent(context, MyReceiver::class.java)
        intent.action = todoItem_id.toString()

        // Alarm Manager
        val am = ContextCompat.getSystemService(
            context,
            AlarmManager::class.java
        ) as AlarmManager

        // Pending Intent
        val pendingIntent = PendingIntent.getBroadcast(
            context, todoItem_id, intent, 0
        )

        // Cancel founded Pending Intent
        pendingIntent.cancel()
        am.cancel(pendingIntent)
        Log.i("PENDING_INTENT", pendingIntent.toString())
    }
}