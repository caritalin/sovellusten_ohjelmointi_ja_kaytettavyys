package com.example.harjoitus1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*

data class Restaurant(
    val name: String,
    val address: String,
    val rating: Double,
    val cuisine: String
)

class MainActivity : ComponentActivity() {
    // Sample restaurant data
    private val restaurants = listOf(
        Restaurant("The Gourmet Kitchen", "123 Food St.", 4.5, "Italian"),
        Restaurant("Sushi World", "456 Sushi Ave.", 4.8, "Japanese"),
        Restaurant("Taco Paradise", "789 Taco Blvd.", 4.2, "Mexican"),
        Restaurant("Burger Heaven", "321 Burger Ln.", 4.0, "American"),
        Restaurant("Pasta House", "147 Noodle St.", 4.3, "Italian"),
        Restaurant("Spicy Curry Palace", "852 Spice Rd.", 4.7, "Indian"),
        Restaurant("Le Petit Bistro", "963 French St.", 4.6, "French"),
        Restaurant("Wok 'n' Roll", "258 Noodle Ave.", 4.1, "Chinese"),
        Restaurant("Pizza Planet", "159 Slice Blvd.", 3.9, "Italian"),
        Restaurant("The BBQ Shack", "753 Grill Ln.", 4.4, "American"),
        Restaurant("Ramen Kingdom", "357 Ramen St.", 4.9, "Japanese"),
        Restaurant("Café Mocha", "123 Coffee Rd.", 4.0, "Cafe"),
        Restaurant("Viva la Vegan", "456 Green St.", 4.6, "Vegan"),
        Restaurant("El Toro Loco", "789 Fiesta Ave.", 4.3, "Mexican"),
        Restaurant("Dim Sum Delight", "159 Dumpling Ln.", 4.5, "Chinese"),
        Restaurant("The Greek Taverna", "258 Olive St.", 4.7, "Greek"),
        Restaurant("Kebab Palace", "963 Spice St.", 4.3, "Middle Eastern"),
        Restaurant("The Hot Pot Spot", "654 Boil Ave.", 4.2, "Chinese"),
        Restaurant("Falafel Corner", "321 Vegan Blvd.", 4.0, "Middle Eastern"),
        Restaurant("Seaside Sushi", "753 Ocean Ave.", 4.8, "Japanese"),
        Restaurant("The Taco Stand", "987 Fiesta St.", 3.8, "Mexican"),
        Restaurant("Steakhouse Supreme", "654 Meat Ln.", 4.9, "American"),
        Restaurant("Pho Haven", "258 Soup Ave.", 4.4, "Vietnamese"),
        Restaurant("The Sushi Spot", "951 Fish Blvd.", 4.2, "Japanese"),
        Restaurant("The Vegan Joint", "753 Plant Ave.", 4.7, "Vegan")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Setting up Navigation
            val navController = rememberNavController()
            NavHost(navController, startDestination = "restaurant_list") {
                composable("restaurant_list") { RestaurantListView(navController, restaurants) }
                composable("restaurant_detail/{restaurantName}") { backStackEntry ->
                    val restaurantName = backStackEntry.arguments?.getString("restaurantName")
                    restaurantName?.let { name ->
                        val restaurant = getRestaurantByName(name)
                        restaurant?.let { RestaurantDetailView(it) }
                    }
                }
            }
        }
    }

    private fun getRestaurantByName(name: String): Restaurant? {
        return restaurants.find { it.name == name }
    }
}

@Composable
fun RestaurantListView(navController: NavController, restaurants: List<Restaurant>) {
    var searchQuery by remember { mutableStateOf("") }

    // Filter restaurants based on search query
    val filteredRestaurants = restaurants.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Restaurant") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredRestaurants.isEmpty()) {
            Text(text = "No restaurants found", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredRestaurants) { restaurant ->
                    RestaurantItem(restaurant) {
                        navController.navigate("restaurant_detail/${restaurant.name}")
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Add space between items
                }
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = restaurant.name, style = MaterialTheme.typography.titleLarge)
            Text(text = restaurant.address)
            Text(text = "Cuisine: ${restaurant.cuisine}, Rating: ${restaurant.rating}")
        }
    }
}

@Composable
fun RestaurantDetailView(restaurant: Restaurant) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Details for ${restaurant.name}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Address: ${restaurant.address}")
            Text(text = "Cuisine: ${restaurant.cuisine}")
            Text(text = "Rating: ${restaurant.rating}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    RestaurantListView(navController, listOf(
        Restaurant("The Gourmet Kitchen", "123 Food St.", 4.5, "Italian"),
        Restaurant("Sushi World", "456 Sushi Ave.", 4.8, "Japanese")
    ))
}
