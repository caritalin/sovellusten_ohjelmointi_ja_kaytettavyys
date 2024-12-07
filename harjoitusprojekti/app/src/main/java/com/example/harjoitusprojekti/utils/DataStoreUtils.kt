package com.example.harjoitusprojekti.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.harjoitusprojekti.models.ShoppingItem

const val itemKeyPrefix = "item_"  // Prefix for keys of all shopping list items

// Save a shopping item to the DataStore
suspend fun saveToDataStore(dataStore: DataStore<Preferences>, shoppingItem: ShoppingItem) {
    dataStore.edit { preferences ->
        // Use a unique key for each product's properties
        preferences[stringPreferencesKey("$itemKeyPrefix${shoppingItem.id}_title")] = shoppingItem.title
        preferences[doublePreferencesKey("$itemKeyPrefix${shoppingItem.id}_price")] = shoppingItem.price
        preferences[stringPreferencesKey("$itemKeyPrefix${shoppingItem.id}_image")] = shoppingItem.image
    }
}

// Load all shopping items from the DataStore
fun loadFromDataStore(dataStore: DataStore<Preferences>): Flow<List<ShoppingItem>> {
    return dataStore.data.map { preferences ->
        val items = mutableListOf<ShoppingItem>()

        // Iterate through a fixed number of possible items (let's assume 1000 items)
        for (i in 0 until 1000) {
            val title = preferences[stringPreferencesKey("$itemKeyPrefix$i" + "_title")] ?: continue
            val price = preferences[doublePreferencesKey("$itemKeyPrefix$i" + "_price")] ?: continue
            val image = preferences[stringPreferencesKey("$itemKeyPrefix$i" + "_image")] ?: ""

            // Add the item to the list
            items.add(ShoppingItem(i, title, price, image))
        }
        items
    }
}

// Remove a shopping item from the DataStore
suspend fun removeFromDataStore(dataStore: DataStore<Preferences>, shoppingItem: ShoppingItem) {
    dataStore.edit { preferences ->
        preferences.remove(stringPreferencesKey("$itemKeyPrefix${shoppingItem.id}_title"))
        preferences.remove(doublePreferencesKey("$itemKeyPrefix${shoppingItem.id}_price"))
        preferences.remove(stringPreferencesKey("$itemKeyPrefix${shoppingItem.id}_image"))
    }
}
