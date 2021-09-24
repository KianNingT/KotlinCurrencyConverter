package com.example.kotlincurrencyconverter.main

import com.example.kotlincurrencyconverter.data.model.CurrencyResponse
import com.example.kotlincurrencyconverter.util.Resource


//reason why we choose interface is because it is easier to test view model
interface MainRepository {

    suspend fun getRates(base: String): Resource<CurrencyResponse>
}