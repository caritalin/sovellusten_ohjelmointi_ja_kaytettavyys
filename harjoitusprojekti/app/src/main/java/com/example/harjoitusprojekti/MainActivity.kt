package com.example.harjoitusprojekti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.compose.ui.res.stringResource
import java.util.Locale
import androidx.compose.material.icons.filled.Translate


import com.example.harjoitusprojekti.ProductListScreen



import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List

import androidx.compose.material.icons.filled.ShoppingCart


import androidx.compose.material.icons.Icons
import coil.compose.rememberImagePainter  // Add this import for image loading
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

import androidx.compose.foundation.Image


// DataStore extension
val ComponentActivity.dataStore by preferencesDataStore(name = "shopping_list")


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
        preferences[priceKey] = shoppingItem.price
        preferences[imageKey] = shoppingItem.image
    }
}



fun loadFromDataStore(dataStore: DataStore<Preferences>): Flow<List<ShoppingItem>> {
    return dataStore.data.map { preferences ->
        val title = preferences[titleKey]
        val price = preferences[priceKey]
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
        Spacer(modifier = Modifier.height(32.dp))
        Text(stringResource(id = R.string.home_title), style = MaterialTheme.typography.titleLarge)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            var selectedLanguage by remember { mutableStateOf(AppLanguage.ENGLISH) }

            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(id = R.string.app_name)) },
                            actions = {
                                IconButton(onClick = { navController.navigate("home") }) {
                                    Icon(Icons.Filled.Home, contentDescription = "Home")
                                }
                                IconButton(onClick = { navController.navigate("product_list") }) {
                                    Icon(Icons.Filled.List, contentDescription = "Product List")
                                }
                                IconButton(onClick = { navController.navigate("shopping_list") }) {
                                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Shopping List")
                                }
                                IconButton(onClick = {
                                    selectedLanguage = if (selectedLanguage == AppLanguage.ENGLISH) {
                                        AppLanguage.FINNISH
                                    } else {
                                        AppLanguage.ENGLISH
                                    }
                                    setLanguage(this@MainActivity, selectedLanguage)  // Kielen vaihto
                                }) {
                                    Icon(Icons.Default.Translate, contentDescription = stringResource(R.string.switch_language))
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
                        composable("shopping_list") { ShoppingListScreen(this@MainActivity) }
                        composable("product_list") { ProductListScreen(navController) }
                    }
                }
            }
        }
    }
}


// Päivitetään AppLanguage enum kielen määrittämiseksi
enum class AppLanguage(val locale: Locale) {
    ENGLISH(Locale("en")),
    FINNISH(Locale("fi"))
}

// Kielen asettaminen: päivitys resurssit uudelle kielelle
fun setLanguage(activity: ComponentActivity, language: AppLanguage) {
    val locale = language.locale
    Locale.setDefault(locale)
    val config = activity.resources.configuration
    config.setLocale(locale)
    activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
    activity.recreate()  // Tämä päivittää Activityn ja UI:n

}

val imageKey = stringPreferencesKey("image_key")
val titleKey = stringPreferencesKey("title_key")
val priceKey = doublePreferencesKey("price_key")



@Composable
fun ShoppingListScreen(activity: ComponentActivity) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    var shoppingList by remember { mutableStateOf<List<ShoppingItem>>(emptyList()) }

    // Ladataan ostoslista DataStoresta
    LaunchedEffect(Unit) {
        loadFromDataStore(activity?.dataStore ?: return@LaunchedEffect).collect { items ->
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
                        // Tuotteen kuva
                        Image(
                            painter = rememberImagePainter(item.image),
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(MaterialTheme.shapes.small)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        // Tuotteen tiedot: nimi, hinta ja kuvaus
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                            )
                            Text(
                                text = "$${item.price}", // Näyttää hinnan
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        // Roskakori-painike
                        IconButton(
                            onClick = {
                                // Poista tuote ostoslistalta ja DataStoresta
                                coroutineScope.launch {
                                    val updatedList = shoppingList.filterNot { it == item }
                                    shoppingList = updatedList
                                    updatedList.forEach { saveToDataStore(activity?.dataStore ?: return@forEach, it) }
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
                }
            }
        }
    }
}
