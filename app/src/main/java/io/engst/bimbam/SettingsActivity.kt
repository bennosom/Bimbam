package io.engst.bimbam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.ui.Modifier
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
        Card {
          AlarmSettings(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp))
        }
      }
    }
  }
}
