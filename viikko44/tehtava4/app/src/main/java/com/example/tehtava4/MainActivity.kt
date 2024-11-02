package com.example.tehtava4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Säädatan malli
data class WeatherData(
    val description: String,
    val temperature: Float,
    val windSpeed: Float
)

// Retrofit API -rajapinta
interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

// JSON-datan malli
data class WeatherResponse(
    val weather: List<WeatherDescription>,
    val main: MainData,
    val wind: WindData
)

data class WeatherDescription(val description: String)
data class MainData(val temp: Float)
data class WindData(val speed: Float)

// Retrofit-instanssin luonti
object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}

// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen("Helsinki")
                }
            }
        }
    }
}

// Sää-näyttö
@Composable
fun WeatherScreen(city: String) {
    var weatherData by remember { mutableStateOf<WeatherData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getWeather(city, "a240e2ff1c9c0d936c91e2a04f73beab")
                weatherData = WeatherData(
                    description = response.weather[0].description,
                    temperature = response.main.temp,
                    windSpeed = response.wind.speed
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        weatherData?.let { data ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color(0xFFE1F5FE),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "Weather in $city",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0277BD)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description: ${data.description.capitalize()}",
                    fontSize = 18.sp,
                    color = Color(0xFF0288D1)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Temperature: ${data.temperature}°C",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Wind Speed: ${data.windSpeed} m/s",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } ?: CircularProgressIndicator()
    }
}
