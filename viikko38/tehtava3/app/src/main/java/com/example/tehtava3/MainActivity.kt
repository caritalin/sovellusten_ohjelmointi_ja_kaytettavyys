package com.example.tehtava3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldExample()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ylävalikko") }
            )
        },
        bottomBar = {
            BottomAppBar {
                Text("Alavalikko", modifier = Modifier.padding(horizontal = 16.dp))
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tervetuloa sovellukseen!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Toiminto */ }) {
                    Text("Paina minua")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewScaffoldExample() {
    ScaffoldExample()
}
