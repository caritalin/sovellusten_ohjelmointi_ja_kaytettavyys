package com.example.tehtava3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.first // Import the Flow extension

// Create a DataStore instance
val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

    // Function to save data to DataStore using lifecycleScope
    fun saveData(context: Context, text: String, number: Int) {
        val textKey = stringPreferencesKey("user_text")
        val numberKey = intPreferencesKey("user_number")

        // Use lifecycleScope from the Activity to launch the coroutine
        lifecycleScope.launch {
            context.dataStore.edit { preferences ->
                preferences[textKey] = text
                preferences[numberKey] = number
            }
        }
    }

    // Function to retrieve saved String data from DataStore
    suspend fun getSavedStringData(context: Context, key: Preferences.Key<String>): String? {
        val preferences = context.dataStore.data.first() // Use 'first()' to get the first value from the flow
        return preferences[key]
    }

    // Function to retrieve saved Int data from DataStore
    suspend fun getSavedIntData(context: Context, key: Preferences.Key<Int>): Int? {
        val preferences = context.dataStore.data.first() // Use 'first()' to get the first value from the flow
        return preferences[key]
    }
}

@Composable
fun MyApp() {
    var text by remember { mutableStateOf("") }
    var number by remember { mutableStateOf(0) }

    val context = LocalContext.current

    // Fetch stored data when the app starts
    LaunchedEffect(Unit) {
        // Retrieve saved data using 'getSavedStringData' and 'getSavedIntData' functions from 'MainActivity'
        text = (context as? MainActivity)?.getSavedStringData(context, stringPreferencesKey("user_text")) ?: ""
        number = (context as? MainActivity)?.getSavedIntData(context, intPreferencesKey("user_number")) ?: 0
    }

    // UI for input
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(value = text, onValueChange = { text = it }, label = { Text("Enter Text") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = number.toString(), onValueChange = { number = it.toIntOrNull() ?: 0 }, label = { Text("Enter Number") })

        Spacer(modifier = Modifier.height(16.dp))

        // Button to save data
        Button(onClick = {
            // Save to DataStore
            (context as? MainActivity)?.saveData(context, text, number)
        }) {
            Text("Save Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the saved data
        Text("Saved Text: $text")
        Text("Saved Number: $number")
    }
}
