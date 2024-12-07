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
import com.example.harjoitusprojekti.RetrofitInstance
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import android.content.Intent
import com.example.harjoitusprojekti.R
import androidx.compose.ui.res.stringResource
import android.content.Context
import androidx.compose.material3.Text
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore

import com.example.harjoitusprojekti.models.ShoppingItem
import com.example.harjoitusprojekti.utils.saveToDataStore

import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavHostController, dataStore: DataStore<Preferences>) {
    val products = remember { mutableStateOf<List<Product>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Lataa tuotteet
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            val fetchedProducts = RetrofitInstance.api.getProducts()
            products.value = fetchedProducts  // Ensure we only set products after data is fetched
            isLoading.value = false
        } catch (e: Exception) {
            isLoading.value = false
            errorMessage.value = e.message // Safe assignment of error message
        }
    }

    Column {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.products_screen_title)) },
        )

        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
        } else if (errorMessage.value != null) {
            val errorMsg = errorMessage.value ?: stringResource(id = R.string.error_message_default)
            Text(text = stringResource(id = R.string.error_message, errorMsg), color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(products.value) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = product.image),
                                    contentDescription = product.title,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .align(Alignment.CenterHorizontally)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        openUrlInBrowser(navController.context, product.image)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .height(32.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.view_photo),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = product.title,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "${product.price} USD",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        // Call the suspend function within a coroutine scope
                                        val shoppingItem = ShoppingItem(
                                            id = product.id,
                                            title = product.title,
                                            price = product.price,
                                            image = product.image
                                        )
                                        coroutineScope.launch {
                                            saveToDataStore(dataStore, shoppingItem)
                                        }
                                        navController.navigate("shopping_list")
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.ShoppingCart,
                                        contentDescription = stringResource(id = R.string.add_to_cart)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun openUrlInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = android.net.Uri.parse(url)
    }
    context.startActivity(intent)
}
