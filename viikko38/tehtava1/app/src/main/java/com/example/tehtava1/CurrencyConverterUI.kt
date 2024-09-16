package com.example.tehtava1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CurrencyConverterUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // Keskittää kaikki komponentit vaakasuunnassa
    ) {
        Title() // Otsikko
        Spacer(modifier = Modifier.height(16.dp)) // Välitila otsikon ja syöttökentän välillä

        InputField() // Tekstinsyöttölaatikko
        Spacer(modifier = Modifier.height(16.dp)) // Välitila syöttökentän ja napin välillä

        ConvertButton() // Nappi
        Spacer(modifier = Modifier.height(16.dp)) // Välitila napin ja muunnettavan summan tekstin välillä

        // Tämä Text-komponentti on vain esimerkki
        Text(
            text = "Muunnettu summa: ",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
