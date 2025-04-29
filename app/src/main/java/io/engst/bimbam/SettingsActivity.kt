package io.engst.bimbam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
        Surface(
          shape = MaterialTheme.shapes.medium,
          modifier = Modifier
            .padding(vertical = 8.dp)
            .imePadding()
        ) {
          AlarmSettings(modifier = Modifier.padding(horizontal = 16.dp))
        }
      }
    }
  }
}
