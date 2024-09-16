package com.example.tehtava1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputField() {
    var input by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth() // Tekstikenttä täyttää koko leveyden
            .padding(horizontal = 16.dp) // Lisää vaakasuuntainen tilaa tekstikentän ympärille
            .padding(vertical = 20.dp) // Lisää pystysuuntainen tilaa tekstikentän ympärille
    ) {
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Syötä summa") },
            modifier = Modifier
                .fillMaxWidth() // Tekstikenttä täyttää Boxin leveyden
                .height(56.dp) // Asettaa tekstikentän korkeuden
                .padding(horizontal = 16.dp) // Lisää tilaa tekstikentän sisällä
        )
    }
}
