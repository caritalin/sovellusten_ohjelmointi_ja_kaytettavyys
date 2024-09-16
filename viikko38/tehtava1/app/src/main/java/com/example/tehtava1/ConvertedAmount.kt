package com.example.tehtava1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConvertedAmount() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Muunnettu summa: ",
            style = MaterialTheme.typography.bodyLarge // Tämä rivi käyttää MaterialTheme
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
