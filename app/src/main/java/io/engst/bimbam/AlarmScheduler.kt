package io.engst.bimbam

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import io.engst.bimbam.ui.AlarmActivity
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class AlarmScheduler(private val context: Context) {

  fun scheduleAlarm(everyMinutes: Int, message: String) {
    ensureExactAlarmPermission(context)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val uiIntent =
      Intent(context, AlarmActivity::class.java).apply {
        putExtra("message", message)
        putExtra("everyMinutes", everyMinutes)
      }
    val pendingIntent =
      PendingIntent.getActivity(
        context,
        0,
        uiIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
      )

    val alarmTime =
      LocalDateTime.now().plusMinutes(everyMinutes.toLong()).truncatedTo(ChronoUnit.MINUTES)
    log { "Scheduling alarm at $alarmTime" }

    Toast.makeText(context, "Alarm scheduled at $alarmTime", Toast.LENGTH_LONG).show()

    val triggerAtMillis = alarmTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val alarmInfo = AlarmManager.AlarmClockInfo(triggerAtMillis, pendingIntent)
    alarmManager.setAlarmClock(alarmInfo, pendingIntent)
  }

  fun cancelAlarm() {
    log { "Cancelling alarm" }
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancelAll()

    Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show()
  }

  private fun ensureExactAlarmPermission(context: Context) {
    val alarmManager = context.getSystemService(AlarmManager::class.java)

    if (alarmManager.canScheduleExactAlarms()) return

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
