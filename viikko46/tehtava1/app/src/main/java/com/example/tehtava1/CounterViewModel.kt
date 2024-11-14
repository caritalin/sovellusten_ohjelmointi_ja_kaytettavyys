package com.example.tehtava1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    private val _counter = MutableLiveData(0)
    val counter: LiveData<Int> = _counter

    fun increase() {
        _counter.value = (_counter.value ?: 0) + 1
    }

    fun decrease() {
        _counter.value = (_counter.value ?: 0) - 1
    }
}
