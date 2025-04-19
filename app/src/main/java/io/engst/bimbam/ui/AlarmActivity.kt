package io.engst.bimbam.ui

import android.media.RingtoneManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import io.engst.bimbam.AlarmScheduler
import io.engst.bimbam.log
import io.engst.bimbam.ui.theme.BimbamTheme

class AlarmActivity : ComponentActivity() {

  private var ringtone: android.media.Ringtone? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val message = intent.getStringExtra("message") ?: "Timeout!"
    val everyMinutes = intent.getIntExtra("everyMinutes", 0).takeIf { it > 0 }
    log { "onCreate: message=$message everyMinutes=$everyMinutes" }

    enableEdgeToEdge()
    setContent {
      BimbamTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent) {
          innerPadding ->
          val context = LocalContext.current
          val scheduler = remember { AlarmScheduler(context) }

          AlarmClock(
            modifier = Modifier.padding(innerPadding),
            message = message,
            onNext = {
              everyMinutes?.let { scheduler.scheduleAlarm(it, message) }
              finish()
            },
            onDismiss = { finish() },
          )
        }
      }
    }
  }

  override fun onStart() {
    super.onStart()

    val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    ringtone = RingtoneManager.getRingtone(this, ringtoneUri).apply { isLooping = true }
    ringtone?.play()

    val vibrator = getSystemService(Vibrator::class.java)
    val timings: LongArray = longArrayOf(50, 50, 50, 50, 50, 100, 350, 25, 25, 25, 25, 200)
    val amplitudes: IntArray = intArrayOf(33, 51, 75, 113, 170, 255, 0, 38, 62, 100, 160, 255)
    val repeatIndex = 0
    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex))
  }

  override fun onStop() {
    super.onStop()

    ringtone?.stop()
    ringtone = null

    getSystemService(Vibrator::class.java).cancel()
  }
}
