package com.example.tehtava1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tehtava1.ui.theme.Tehtava1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tehtava1Theme {
                CounterScreen()
            }
        }
    }
}

@Composable
fun CounterScreen() {
    // Laskurin tila
    var counter by remember { mutableStateOf(0) }

    // Box-komponentti, joka keskittää sisällön
    Box(
        modifier = Modifier
            .fillMaxSize()  // Box täyttää koko näytön
            .padding(16.dp),  // Lisätään pieni marginaali reunoihin
        contentAlignment = Alignment.Center  // Keskitetään sisältö
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Keskittää sisällön vaakasuunnassa
            verticalArrangement = Arrangement.spacedBy(20.dp),  // Lisää väliä komponenttien väliin
            modifier = Modifier.fillMaxWidth()  // Column täyttää vaakasuunnassa koko ruudun
        ) {
            // Laskurin näyttö
            Text(
                text = "Counter: $counter",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            // "Kasvata"-painike
            Button(onClick = { counter++ }, modifier = Modifier.fillMaxWidth()) {
                Text("Kasvata")
            }

            // "Pienennä"-painike
            Button(onClick = { counter-- }, modifier = Modifier.fillMaxWidth()) {
                Text("Pienennä")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Tehtava1Theme {
        CounterScreen()
    }
}
