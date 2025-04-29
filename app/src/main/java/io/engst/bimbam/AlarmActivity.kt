package io.engst.bimbam

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Card
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.engst.bimbam.core.log
import io.engst.bimbam.model.AlarmScheduler
import io.engst.bimbam.ui.AlarmClock
import io.engst.bimbam.ui.theme.BimbamTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AlarmActivity : ComponentActivity() {

  val scope = CoroutineScope(SupervisorJob())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    scope.launch {
      AlarmService.activityStop.collect {
        log { "AlarmService.activityStop: $it" }
        finish()
      }
    }
    window.addFlags(
      WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
              WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
              WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
    )
    enableEdgeToEdge()

    val message = intent.getStringExtra("message") ?: "Timeout!"
    val everyMinutes = intent.getIntExtra("everyMinutes", 0).takeIf { it > 0 }
    log { "onCreate: message=$message everyMinutes=$everyMinutes" }

    setContent {
      BimbamTheme {
        Card(modifier = Modifier
          .fillMaxSize()
          .windowInsetsPadding(WindowInsets.safeDrawing)) {
          val context = LocalContext.current
          val scheduler = remember { AlarmScheduler(context) }

          AlarmClock(
            modifier = Modifier.padding(16.dp),
            message = message,
            onNext = {
              stopService(
                Intent(this@AlarmActivity, AlarmService::class.java).apply { action = "stop_alarm" }
              )
              everyMinutes?.let { scheduler.scheduleAlarm(it, message) }
              finish()
            },
            onDismiss = {
              stopService(
                Intent(this@AlarmActivity, AlarmService::class.java).apply { action = "stop_alarm" }
              )
              finish()
            },
            onSilent = {
              stopService(
                Intent(this@AlarmActivity, AlarmService::class.java).apply {
                  action = "silent_alarm"
                }
              )
            },
          )
        }
      }
    }
  }

  override fun onStart() {
    super.onStart()
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onDestroy() {
    super.onDestroy()
    scope.cancel()
  }
}
