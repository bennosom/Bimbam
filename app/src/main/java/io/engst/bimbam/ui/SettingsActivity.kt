package io.engst.bimbam.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.engst.bimbam.ui.theme.BimbamTheme

class SettingsActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    requestPermissions(arrayOf("android.permission.SCHEDULE_EXACT_ALARM"), 0)

    enableEdgeToEdge()
    setContent {
      BimbamTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent) {
          innerPadding ->
          AlarmSettings(Modifier.padding(innerPadding))
        }
      }
    }
  }
}
