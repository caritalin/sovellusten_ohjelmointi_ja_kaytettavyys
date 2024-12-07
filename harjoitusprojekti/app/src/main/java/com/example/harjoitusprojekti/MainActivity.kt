package com.example.harjoitusprojekti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx. compose. material. icons. automirrored. filled. List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.rememberNavController
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

import com.example.harjoitusprojekti.screens.HomeScreen
import com.example.harjoitusprojekti.screens.ProductListScreen
import com.example.harjoitusprojekti.screens.ShoppingListScreen

val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(id = R.string.app_name)) },
                            actions = {
                                IconButton(onClick = { navController.navigate("home") }) {
                                    Icon(Icons.Default.Home, contentDescription = "Home")
                                }
                                IconButton(onClick = { navController.navigate("product_list") }) {
                                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Product List")
                                }
                                IconButton(onClick = { navController.navigate("shopping_list") }) {
                                    Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping List")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen(navController) }
                        composable("product_list") { ProductListScreen(navController) }
                        composable("shopping_list") { ShoppingListScreen(this@MainActivity) }
                    }
                }
            }
        }

        // Lue ja tallenna tietoa DataStoreen
        val coroutineScope = lifecycleScope
        coroutineScope.launch {
            // Lue arvo DataStoresta
            val preferences = dataStore.data.first()
            val savedValue = preferences[stringPreferencesKey("user_setting")] ?: "default_value"
            println("Saved value: $savedValue")  // Voit käyttää tätä arvon näyttämiseen tai muuhun logiikkaan

            // Tallenna arvo DataStoreen
            dataStore.edit { settings ->
                settings[stringPreferencesKey("user_setting")] = "new_value"
            }
        }
    }
}
