package com.example.tehtava2

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    // MutableState to hold the seconds
    private val _seconds = mutableStateOf(0)
    val seconds: Int get() = _seconds.value

    private var isRunning = false // To track if timer is running

    // Start or resume the timer
    fun startTimer() {
        if (!isRunning) {
            isRunning = true
            viewModelScope.launch {
                while (isRunning) {
                    delay(1000) // Wait for 1 second
                    _seconds.value += 1 // Increment seconds
                }
            }
        }
    }

    // Stop the timer
    fun stopTimer() {
        isRunning = false
    }

    // Reset the timer
    fun resetTimer() {
        _seconds.value = 0
    }
}
