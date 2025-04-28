package io.engst.bimbam.model

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import io.engst.bimbam.AlarmService
import io.engst.bimbam.core.log
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm(everyMinutes: Int, message: String) {
        if (!alarmManager.canScheduleExactAlarms()) {
            log { "Exact alarm permission not granted" }
            return requestExactAlarmPermission()
        }

        val serviceIntent =
            Intent(context, AlarmService::class.java).apply {
                putExtra("message", message)
                putExtra("everyMinutes", everyMinutes)
            }
        val servicePendingIntent =
            PendingIntent.getForegroundService(
                context,
                0,
                serviceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        val alarmTime =
            LocalDateTime.now().plusMinutes(everyMinutes.toLong()).truncatedTo(ChronoUnit.MINUTES)
        log { "Scheduling alarm at $alarmTime" }

        Toast.makeText(context, "Alarm scheduled at $alarmTime", Toast.LENGTH_LONG).show()

        val triggerAtMillis = alarmTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val alarmInfo = AlarmManager.AlarmClockInfo(triggerAtMillis, servicePendingIntent)
        alarmManager.setAlarmClock(alarmInfo, servicePendingIntent)
    }

    fun cancelAlarm() {
        log { "Cancelling alarm" }
        alarmManager.cancelAll()

        Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show()
    }

    private fun requestExactAlarmPermission() {
        val intent =
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = "package:${context.packageName}".toUri()
            }
        if (context is Activity) {
            context.startActivityForResult(intent, 1234)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
