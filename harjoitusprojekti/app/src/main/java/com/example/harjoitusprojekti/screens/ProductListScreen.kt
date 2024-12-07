package com.example.harjoitusprojekti.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.harjoitusprojekti.models.Product
import com.example.harjoitusprojekti.models.ShoppingItem
import com.example.harjoitusprojekti.RetrofitInstance
import com.example.harjoitusprojekti.utils.saveToDataStore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context

// In your MainActivity or relevant Activity
val Context.dataStore by preferencesDataStore(name = "shopping_data")

@Composable
fun ProductListScreen(navController: NavHostController) {
    val products = remember { mutableStateOf<List<Product>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val isAddingToCart = remember { mutableStateOf(false) }

    // LaunchedEffect joka lataa tuotteet
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            products.value = RetrofitInstance.api.getProducts()
            isLoading.value = false
        } catch (e: Exception) {
            isLoading.value = false
            errorMessage.value = e.message
        }
    }

    // UI-koodisi pysyy samana
    if (isLoading.value) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
    } else if (errorMessage.value != null) {
        Text(text = "Error: ${errorMessage.value}", color = MaterialTheme.colorScheme.error)
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(products.value) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = product.image),
                            contentDescription = product.title,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = product.title, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "${product.price} USD",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Lisää tuotteita ostoskoriin käyttäen LaunchedEffect
                        IconButton(
                            onClick = {
                                // LaunchedEffect suoraan onClickissä
                                isAddingToCart.value = true
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Add to Cart")
                        }

                        // LaunchedEffect joka suoritetaan, kun tuote lisätään ostoskoriin
                        if (isAddingToCart.value) {
                            LaunchedEffect(isAddingToCart.value) {
                                val shoppingItem = ShoppingItem(
                                    id = product.id,
                                    title = product.title,
                                    price = product.price,
                                    image = product.image
                                )
                                saveToDataStore(navController.context.dataStore, shoppingItem)
                                isAddingToCart.value = false
                            }
                        }
                    }
                }
            }
        }
    }
}