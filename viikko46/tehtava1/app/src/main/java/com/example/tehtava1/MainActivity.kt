package com.example.tehtava1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import com.example.tehtava1.ui.theme.Tehtava1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tehtava1Theme {
                Surface(color = MaterialTheme.colors.background) {
                    CounterScreen()
                }
            }
        }
    }
}
