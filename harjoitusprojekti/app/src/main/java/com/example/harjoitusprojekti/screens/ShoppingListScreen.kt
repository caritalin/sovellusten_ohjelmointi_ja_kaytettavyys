package com.example.harjoitusprojekti.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.harjoitusprojekti.models.ShoppingItem
import com.example.harjoitusprojekti.utils.loadFromDataStore
import kotlinx.coroutines.launch
import com.example.harjoitusprojekti.utils.removeFromDataStore


@Composable
fun ShoppingListScreen(activity: ComponentActivity) {
    val shoppingList = remember { mutableStateOf<List<ShoppingItem>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        loadFromDataStore(activity.dataStore).collect { items ->
            shoppingList.value = items
        }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(shoppingList.value) { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(model = item.image),
                        contentDescription = item.title,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "${item.price} USD", style = MaterialTheme.typography.bodyMedium)
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            // Poista tuote DataStoresta ja päivitä lista
                            removeFromDataStore(activity.dataStore, item)
                            val updatedList = shoppingList.value.filterNot { it == item }
                            shoppingList.value = updatedList
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Item")
                    }
                }
            }
        }
    }
}
