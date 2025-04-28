package io.engst.bimbam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.engst.bimbam.ui.AlarmSettings
import io.engst.bimbam.ui.theme.BimbamTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf("android.permission.SCHEDULE_EXACT_ALARM"), 0)

        enableEdgeToEdge()
        setContent {
            BimbamTheme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                          .fillMaxSize()
                          .pointerInput(Unit) {
                            detectTapGestures(
                              onTap = {
                                // Handle tap outside to dismiss
                                finish()
                              }
                            )
                          },
                ) {
                    Card(
                        modifier = Modifier
                          .windowInsetsPadding(WindowInsets.safeDrawing)
                          .padding(16.dp)
                    ) {
                        AlarmSettings(
                            modifier = Modifier
                              .fillMaxWidth()
                              .verticalScroll(rememberScrollState())
                              .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
