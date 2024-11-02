package com.example.tehtava3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// TODO-datan malli
data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

// Retrofit API -rajapinta
interface TodoApi {
    @GET("todos")
    suspend fun getTodos(): List<Todo>
}

// Retrofit-instanssin luonti
object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    val api: TodoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)
    }
}

// ViewModel TODO-listalle
class TodoViewModel : ViewModel() {
    var todos by mutableStateOf<List<Todo>>(emptyList())

    suspend fun fetchTodos() {
        todos = RetrofitInstance.api.getTodos()
    }
}

// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tehtava3Theme {
                Surface(modifier = androidx.compose.ui.Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TodoListScreen()
                }
            }
        }
    }
}

// TODO-lista -näyttö
@Composable
fun TodoListScreen(viewModel: TodoViewModel = viewModel()) {
    val todos = viewModel.todos

    // Käytetään LaunchedEffectia fetchTodosin kutsumiseen
    LaunchedEffect(Unit) {
        viewModel.fetchTodos()
    }

    LazyColumn {
        items(todos) { todo ->
            Text(text = todo.title)
        }
    }
}
