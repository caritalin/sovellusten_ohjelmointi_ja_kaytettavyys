package com.example.tehtava2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Lisää tämä rivi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tehtava2.ui.theme.Tehtava2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tehtava2Theme {
                TimerScreen()
            }
        }
    }
}

@Composable
fun TimerScreen() {
    // Getting the ViewModel instance
    val timerViewModel: TimerViewModel = viewModel()

    // Start the timer when the composable is first launched
    LaunchedEffect(true) {
        timerViewModel.startTimer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sekunnit: ${timerViewModel.seconds}", style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp))

        Spacer(modifier = Modifier.height(20.dp))

        // Button to reset the timer
        Button(onClick = { timerViewModel.resetTimer() }) {
            Text(text = "Nollaa")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Button to stop the timer
        Button(onClick = { timerViewModel.stopTimer() }) {
            Text(text = "Pysäytä")
        }
    }
}
