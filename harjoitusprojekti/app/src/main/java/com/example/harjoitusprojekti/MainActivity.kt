package com.example.harjoitusprojekti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.Icons
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import androidx.compose.foundation.Image
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.res.stringResource
import android.content.res.Configuration
import android.content.Context
import java.util.Locale
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Language
import androidx.compose.ui.platform.LocalContext
import android.app.Activity




// DataStore extension
val ComponentActivity.dataStore: DataStore<Preferences> by preferencesDataStore(name = "shopping_list")

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


// Product data class (for the products fetched from API)
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String
)

// Retrofit client setup (you should define this in a separate file)
object RetrofitClient {
    val apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}

// Retrofit API service interface
interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}

suspend fun saveToDataStore(dataStore: DataStore<Preferences>, shoppingItem: ShoppingItem) {
    dataStore.edit { preferences ->
        preferences[titleKey] = shoppingItem.title
        preferences[priceKey] = shoppingItem.price  // Corrected for double type
        preferences[imageKey] = shoppingItem.image
    }
}



fun loadFromDataStore(dataStore: DataStore<Preferences>): Flow<List<ShoppingItem>> {
    return dataStore.data.map { preferences ->
        val title = preferences[titleKey]
        val price = preferences[priceKey] ?: 0.0
        val image = preferences[imageKey] ?: "https://path_to_default_image.com"
        if (title != null && price != null) {
            listOf(ShoppingItem(id = 0, title = title, price = price.toDouble(), image = image))
        } else {
            emptyList()
        }
    }
}


// Home Screen
@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Käytetään stringResourcea merkkijonojen hakemiseen
        Text(
            text = stringResource(id = R.string.welcome_message),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sovelluksen toiminnan kuvaus
        Text(
            text = stringResource(id = R.string.app_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.cart_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.manage_cart),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.simple_app_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var language by remember { mutableStateOf("en") } // State for language selection
            val context = LocalContext.current

            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(id = R.string.app_name)) },
                            actions = {
                                IconButton(onClick = { navController.navigate("home") }) {
                                    Icon(Icons.Filled.Home, contentDescription = stringResource(id = R.string.home))
                                }
                                IconButton(onClick = { navController.navigate("product_list") }) {
                                    Icon(Icons.Filled.List, contentDescription = stringResource(id = R.string.go_to_products))
                                }
                                IconButton(onClick = { navController.navigate("shopping_list") }) {
                                    Icon(Icons.Filled.ShoppingCart, contentDescription = stringResource(id = R.string.go_to_shopping_list))
                                }
                                IconButton(onClick = {
                                    language = if (language == "en") "fi" else "en"
                                    changeLanguage(context, language)
                                }) {
                                    Icon(Icons.Filled.Language, contentDescription = stringResource(id = R.string.change_language))
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    // Päivitetään käyttöliittymä heti kun kieli muuttuu
                    CompositionLocalProvider(LocalContext provides context.createLocalizedContext(language)) {
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") { HomeScreen(navController) }
                            composable("product_list") { ProductListScreen(navController) }
                            composable("shopping_list") { ShoppingListScreen() }
                        }
                    }
                }
            }
        }
    }

    private fun Context.createLocalizedContext(language: String): Context {
        val locale = Locale(language)
        val config = Configuration(this.resources.configuration)
        config.setLocale(locale)
        return this.createConfigurationContext(config)
    }

    private fun changeLanguage(context: Context, language: String) {
        Locale.setDefault(Locale(language))
        val config = Configuration(context.resources.configuration)
        config.setLocale(Locale(language))
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}




@Composable
fun ShoppingListScreen() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current as ComponentActivity // Hanki aktiivisuus kontekstista

    var shoppingList by remember { mutableStateOf<List<ShoppingItem>>(emptyList()) }

    // Lataa ostoslista DataStoresta
    LaunchedEffect(Unit) {
        loadFromDataStore(context.dataStore).collect { items ->
            shoppingList = items
        }
    }

    Column(modifier = Modifier.padding(12.dp)) {
        Text("Shopping List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(shoppingList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = rememberImagePainter(item.image),
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(MaterialTheme.shapes.small)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                            )
                            Text(
                                text = "$${item.price}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    val updatedList = shoppingList.filterNot { it == item }
                                    shoppingList = updatedList
                                    updatedList.forEach { saveToDataStore(context.dataStore, it) }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ProductListScreen(navController: NavHostController) {
    val products = remember { mutableStateOf<List<Product>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope() // Coroutine scope määritellään tässä
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    LaunchedEffect(Unit) {
        try {
            val fetchedProducts = RetrofitClient.apiService.getProducts()
            products.value = fetchedProducts
        } catch (e: Exception) {
            // Handle error
        }
    }

    Column(modifier = Modifier.padding(12.dp)) {
        Text("Product List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(products.value) { product ->
                // Tuote ja ostoskärry samassa rivissä
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.medium),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Tuotekuvan ja tiedon osuus
                        Row(modifier = Modifier.weight(1f)) {
                            // Tuotteen kuva
                            Image(
                                painter = rememberImagePainter(product.image),
                                contentDescription = product.title,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                // Tuotteen nimi
                                Text(
                                    text = product.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = Int.MAX_VALUE, // Ensures the entire title is shown
                                )
                                // Tuotteen hinta
                                Text(
                                    text = "${product.price} USD",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Ostoskärry-painike
                        IconButton(
                            onClick = {
                                // Lisää tuote ostoslistalle
                                val shoppingItem = ShoppingItem(id = product.id, title = product.title, price = product.price, image = product.image)
                                coroutineScope.launch {
                                    saveToDataStore(activity?.dataStore ?: return@launch, shoppingItem)
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Add to Cart")
                        }
                    }

                    // View Photo -nappi
                    Button(onClick = {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(product.image)) // Käytetään tuotteen kuvan URL-osoitetta
                        context.startActivity(webIntent) // Käynnistetään intent
                    }) {
                        Text("View Photo")
                    }
                }
            }
        }
    }
}
