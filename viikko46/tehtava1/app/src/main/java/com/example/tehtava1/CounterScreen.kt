package com.example.tehtava1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text

@Composable
fun CounterScreen() {
    // Local state for counter, managed by Compose
    val counter = remember { mutableStateOf(0) }

    MaterialTheme {
        Surface {
            Column {
                Text(
                    text = "Counter: ${counter.value}",
                    style = MaterialTheme.typography.bodyLarge // Use a valid typography style in Material3
                )
                Button(onClick = { counter.value++ }) {
                    Text("Increase")
                }
                Button(onClick = { counter.value-- }) {
                    Text("Decrease")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCounterScreen() {
    CounterScreen()
}
