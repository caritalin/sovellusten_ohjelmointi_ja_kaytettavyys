package com.example.compose_kayttoliittyma


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
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    var text by remember { mutableStateOf("Click to say Hello!") }

    // Pääkäyttöliittymä
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Yläosan laatikko, jossa lukee "My App"
        Text(
            text = "My App",
            fontSize = 32.sp,
            modifier = Modifier.padding(16.dp)
        )

        // Keskellä oleva teksti, jota vaihdetaan napilla
        Text(
            text = text,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        // Alaosan nappi
        Button(
            onClick = {
                text = if (text == "Click to say Hello!") "Hello!" else "Click to say Hello!"
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Say Hello")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
    MyApp()
}

