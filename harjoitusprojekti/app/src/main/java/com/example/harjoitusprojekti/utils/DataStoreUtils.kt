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

// Save a shopping item to DataStore
suspend fun saveToDataStore(dataStore: DataStore<Preferences>, shoppingItem: ShoppingItem) {
    dataStore.edit { preferences ->
        preferences[stringPreferencesKey("${itemKeyPrefix}${shoppingItem.id}_title")] = shoppingItem.title
        preferences[doublePreferencesKey("${itemKeyPrefix}${shoppingItem.id}_price")] = shoppingItem.price
        preferences[stringPreferencesKey("${itemKeyPrefix}${shoppingItem.id}_image")] = shoppingItem.image
    }
}

// Load all shopping items from DataStore
fun loadFromDataStore(dataStore: DataStore<Preferences>): Flow<List<ShoppingItem>> {
    return dataStore.data.map { preferences ->
        preferences.asMap()
            .filterKeys { it.name.startsWith(itemKeyPrefix) }
            .mapNotNull { entry ->
                val keyName = entry.key.name
                val id = keyName.removePrefix(itemKeyPrefix).substringBefore("_").toIntOrNull() ?: return@mapNotNull null
                val title = preferences[stringPreferencesKey("${itemKeyPrefix}${id}_title")] ?: return@mapNotNull null
                val price = preferences[doublePreferencesKey("${itemKeyPrefix}${id}_price")] ?: return@mapNotNull null
                val image = preferences[stringPreferencesKey("${itemKeyPrefix}${id}_image")] ?: ""
                ShoppingItem(id, title, price, image)
            }
            .distinctBy { it.id } // Ensure no duplicates
    }
}

// Remove a shopping item from DataStore
suspend fun removeFromDataStore(dataStore: DataStore<Preferences>, shoppingItem: ShoppingItem) {
    dataStore.edit { preferences ->
        preferences.remove(stringPreferencesKey("${itemKeyPrefix}${shoppingItem.id}_title"))
        preferences.remove(doublePreferencesKey("${itemKeyPrefix}${shoppingItem.id}_price"))
        preferences.remove(stringPreferencesKey("${itemKeyPrefix}${shoppingItem.id}_image"))
    }
}


