package com.example.tehtava1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Title() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(50.dp)) // Lisää tilaa ylle ennen otsikkoa
        Text(
            text = "Valuuttamuunnin",
            style = MaterialTheme.typography.headlineLarge, // Käyttää oikeaa tekstityyliä
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() // Teksti täyttää koko leveyden
        )
    }
}
