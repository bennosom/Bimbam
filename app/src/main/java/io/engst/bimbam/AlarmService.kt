package io.engst.bimbam

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationCompat
import io.engst.bimbam.model.AcousticFeedback
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AlarmService : Service() {

    companion object {
        private val _activityStop = MutableSharedFlow<Unit>()
        val activityStop = _activityStop.asSharedFlow()
    }

    private val acousticFeedback by lazy { AcousticFeedback(this) }

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == "stop_alarm") {
            _activityStop.tryEmit(Unit)
            stopSelf()
            return START_NOT_STICKY
        }

        // Handle the alarm event here
        val message = intent.getStringExtra("message") ?: "Alarm!"
        val everyMinutes = intent.getIntExtra("everyMinutes", 0).takeIf { it > 0 }

        // Create a notification channel for foreground service
        val channel =
            NotificationChannel(
                "alarm_service_channel",
                "Alarm Service",
                NotificationManager.IMPORTANCE_HIGH,
            )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        // Create an intent to launch the AlarmActivity
        val activityIntent =
            Intent(this, AlarmActivity::class.java).apply {
                putExtra("message", message)
                putExtra("everyMinutes", everyMinutes)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        val fullscreenPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        // Add a stop action to the notification
        val stopIntent = Intent(this, AlarmService::class.java).apply { action = "stop_alarm" }
        val stopPendingIntent =
            PendingIntent.getService(
                this,
                0,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        // Build the full-screen notification
        val notification =
            NotificationCompat.Builder(this, "alarm_service_channel")
                .setContentTitle("Alarm Triggered")
                .setContentText(message)
                .setSmallIcon(R.drawable.baseline_3d_rotation_24) // Replace with your app's icon
                .addAction(
                    R.drawable.baseline_3d_rotation_24,
                    "Stop",
                    stopPendingIntent
                ) // Add stop action
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullscreenPendingIntent, true)
                .build()

        // Show the notification
        startForeground(1, notification)

        acousticFeedback.play()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources if needed
        acousticFeedback.stop()
    }
}
