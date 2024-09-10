package com.example.omasovellus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.omasovellus.ui.theme.OmaSovellusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                    //Asetetaan sovellukselle käyttöliittymä, UI (Jetpack Composse -pääfunktio)
                }
            }
        }


// Moderni UI -kehitys funktionaalista
// Pätee: React/React Native, Flutter, Android Jetpack Compose, MAUI, SwiftUI, QML jne ...
// Sovelluksella käyttöliittymäkomponentit (Android: composaples)
// Ja tila: React useState, Compose remember

// Tehdään composaple, joka näyttää Hello World
@Composable
fun Hello() {
    Text( text = "Hello World")
}

