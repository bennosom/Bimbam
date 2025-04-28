package io.engst.bimbam

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
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
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize(),
                    containerColor = Color.Companion.Transparent,
                ) { innerPadding ->
                    val context = LocalContext.current
                    val scheduler = remember { AlarmScheduler(context) }

                    AlarmClock(
                        modifier = Modifier.Companion.padding(innerPadding),
                        message = message,
                        onNext = {
                            stopService(Intent(this, AlarmService::class.java).apply {
                                action = "stop_alarm"
                            })
                            everyMinutes?.let { scheduler.scheduleAlarm(it, message) }
                            finish()
                        },
                        onDismiss = {
                            stopService(Intent(this, AlarmService::class.java).apply {
                                action = "stop_alarm"
                            })
                            finish()
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
