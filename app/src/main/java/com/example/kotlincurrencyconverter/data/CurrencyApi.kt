package com.example.kotlincurrencyconverter.data

import com.example.kotlincurrencyconverter.data.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("1ce038ac043048061d2da823/latest/SGD")
    suspend fun getRates(
        @Query("base") base: String
    ) : Response<CurrencyResponse>
}