package com.example.lokalisointi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.lokalisointi.ui.theme.LokalisointiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LokalisointiTheme {
                BloodPressureScreen() // Use BloodPressureScreen directly
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodPressureScreen() {
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    var interpretation by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.title_blood_pressure), color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Blue)
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = systolic,
                        onValueChange = { systolic = it },
                        label = { Text(stringResource(id = R.string.systolic_value)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = diastolic,
                        onValueChange = { diastolic = it },
                        label = { Text(stringResource(id = R.string.diastolic_value)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            interpretation = interpretBloodPressure(systolic.toIntOrNull(), diastolic.toIntOrNull())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(id = R.string.evaluate_button))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = interpretation,
                        fontSize = 24.sp,
                        color = Color.Blue
                    )
                }
            }
        }
    )
}

fun interpretBloodPressure(systolic: Int?, diastolic: Int?): String {
    return when {
        systolic == null || diastolic == null -> "Anna kelvolliset arvot"
        systolic < 90 || diastolic < 60 -> "Matala verenpaine"
        systolic in 90..120 && diastolic in 60..80 -> "Hyvä verenpaine"
        systolic in 121..139 || diastolic in 81..89 -> "Lievästi koholla"
        systolic >= 140 || diastolic >= 90 -> "Koholla"
        else -> "Tuntematon virhe"
    }
}
