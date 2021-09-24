package com.example.kotlincurrencyconverter.main

import com.example.kotlincurrencyconverter.data.CurrencyApi
import com.example.kotlincurrencyconverter.data.model.CurrencyResponse
import com.example.kotlincurrencyconverter.util.Resource
import java.lang.Exception
import javax.inject.Inject

//repository to make request and handle response
class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
): MainRepository {

    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            //now the result will contain all the rates that we are interested in
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else{
                //else not successful or doesn't have a body
                    //if error, then return resource error with error message from API
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}