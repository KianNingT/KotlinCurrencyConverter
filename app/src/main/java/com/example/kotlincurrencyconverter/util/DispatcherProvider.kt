package com.example.kotlincurrencyconverter.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    //for real usage, different dispatchers for different real usage.
    //for testing view model, we can pass the test dispatchers for all of these
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}