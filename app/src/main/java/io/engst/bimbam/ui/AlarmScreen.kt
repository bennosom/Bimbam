package io.engst.bimbam.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.engst.bimbam.ui.theme.BimbamTheme

@Composable
fun AlarmClock(
  message: String,
  modifier: Modifier = Modifier,
  onNext: () -> Unit,
  onDismiss: () -> Unit = {},
) {
  Column(
    modifier = modifier.fillMaxSize().padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Card(modifier = Modifier.fillMaxWidth().weight(2f)) {
      Text(
        modifier = Modifier.fillMaxWidth().weight(2f).wrapContentHeight(Alignment.CenterVertically),
        style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center),
        text = message,
      )
    }
    Button(
      modifier = Modifier.fillMaxWidth().weight(1f),
      shape = RoundedCornerShape(32.dp),
      onClick = { onNext() },
    ) {
      Text(
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.Center,
        text = "Next",
      )
    }
    Button(
      modifier = Modifier.fillMaxWidth().weight(1f),
      onClick = { onDismiss() },
    ) {
      Text(
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        text = "Cancel",
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun AlarmClockPreview() {
  BimbamTheme { AlarmClock("Touch to dismiss!", onNext = {}, onDismiss = {}) }
}
