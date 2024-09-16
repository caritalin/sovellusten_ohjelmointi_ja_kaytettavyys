package com.example.tehtava1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConvertButton() {
    Button(
        onClick = { /* Placeholder for conversion */ },
        modifier = Modifier
            .width(150.dp) // Asettaa napin leveyden
            .height(50.dp) // Asettaa napin korkeuden
            .padding(vertical = 8.dp) // Lisää pystysuuntaista marginaalia napin ympärille
    ) {
        Text("Muunna")
    }
}
