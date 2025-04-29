package io.engst.bimbam.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.engst.bimbam.model.AlarmScheduler
import io.engst.bimbam.ui.theme.BimbamTheme
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(
  ExperimentalPermissionsApi::class,
  ExperimentalLayoutApi::class,
  ExperimentalFoundationApi::class,
)
@Composable
fun AlarmSettings(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Spacer(Modifier.size(8.dp))
    val context = LocalContext.current
    val scheduler = remember { AlarmScheduler(context) }
    Text(
      "Remind to".uppercase(Locale.getDefault()),
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onSurface,
    )
    var message by remember { mutableStateOf("Move, Breathe, Relax") }
    val bringRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    BasicTextField(
      value = message,
      onValueChange = {
        message = it
        scope.launch {
          bringRequester.bringIntoView() // nur hier manuell scrollen (bei Texteingabe)
        }
      },
      textStyle = MaterialTheme.typography.titleLarge,
      modifier =
        Modifier
          .fillMaxWidth()
          .bringIntoViewRequester(bringRequester)
          .background(MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.small),
      decorationBox = { innerTextField ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(Modifier.weight(1f)) { innerTextField() }
          Icon(
            Icons.Default.Clear,
            null,
            modifier = Modifier.clickable { message = "" },
            tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
          )
        }
      },
    )
    Spacer(Modifier.padding(0.dp))
    Text(
      "every".uppercase(Locale.getDefault()),
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onSurface,
    )
    var everyMinutes by remember { mutableIntStateOf(60) }
    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
      verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
      listOf(2, 5, 10, 15, 30, 45, 60, 90, 120).forEach {
        key(it) {
          FilterChip(
            onClick = { everyMinutes = it },
            label = { Text("$it ${if (it == 1) "minute" else "minutes"}") },
            selected = everyMinutes == it,
          )
        }
      }
    }
    Button(onClick = { scheduler.scheduleAlarm(everyMinutes, message) }) {
      Text(
        modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
        style = MaterialTheme.typography.displayMedium,
        text = "Go",
      )
    }
    Button(onClick = { scheduler.cancelAlarm() }) {
      Text(style = MaterialTheme.typography.bodyLarge, text = "Stop")
    }
    Spacer(Modifier.size(8.dp))
  }
}

@Preview(showBackground = true)
@Composable
fun AlarmSettingsPreview() {
  BimbamTheme { AlarmSettings() }
}
