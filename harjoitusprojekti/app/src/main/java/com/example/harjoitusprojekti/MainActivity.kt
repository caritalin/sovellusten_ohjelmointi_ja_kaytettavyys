package com.example.harjoitusprojekti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*
import androidx.datastore.core.DataStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


// DataStore extension
val ComponentActivity.dataStore by preferencesDataStore(name = "shopping_list")

// Data keys for DataStore
val imageKey = stringPreferencesKey("image_key")
val titleKey = stringPreferencesKey("title_key")
val priceKey = doublePreferencesKey("price_key")

// Shopping List Item
data class ShoppingItem(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String
)

// Product class for API data
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String
)

// Retrofit API interface
interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}

// Retrofit instance
object RetrofitInstance {
    val api: ProductApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}

// Save a shopping item to DataStore
suspend fun saveToDataStore(dataStore: DataStore<Preferences>, shoppingItem: ShoppingItem) {
    dataStore.edit { preferences ->
        preferences[titleKey] = shoppingItem.title
        preferences[priceKey] = shoppingItem.price
        preferences[imageKey] = shoppingItem.image
    }
}

// Load shopping list from DataStore
fun loadFromDataStore(dataStore: DataStore<Preferences>): Flow<List<ShoppingItem>> {
    return dataStore.data.map { preferences ->
        val title = preferences[titleKey] ?: return@map emptyList()
        val price = preferences[priceKey] ?: return@map emptyList()
        val image = preferences[imageKey] ?: ""
        listOf(ShoppingItem(id = 0, title = title, price = price, image = image))
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.home_title),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun ProductListScreen(navController: NavHostController) {
    val products = remember { mutableStateOf<List<Product>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalContext.current as? ComponentActivity

    LaunchedEffect(Unit) {
        try {
            products.value = RetrofitInstance.api.getProducts()
        } catch (e: Exception) {
            // Handle error
        }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(products.value) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = rememberImagePainter(data = product.image),
                        contentDescription = product.title,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.title, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "${product.price} USD",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val shoppingItem = ShoppingItem(
                                    id = product.id,
                                    title = product.title,
                                    price = product.price,
                                    image = product.image
                                )
                                saveToDataStore(activity?.dataStore ?: return@launch, shoppingItem)
                            }
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Add to Cart")
                    }
                }
            }
        }
    }
}

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
                        painter = rememberImagePainter(data = item.image),
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
                                    Icon(Icons.Default.List, contentDescription = "Product List")
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
    }
}
